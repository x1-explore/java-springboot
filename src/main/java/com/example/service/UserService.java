package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {

    private final String uploadPath;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.uploadPath = "uploads/avatars";
    }

    @PostConstruct
    public void init() {
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 检查并创建访客账号
        createGuestUserIfNotFound();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 尝试通过用户名查找
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // 如果用户名不存在，尝试通过邮箱查找
                    return userRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
                });
        
        // 确保用户角色不为空
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        
        return user;
    }

    /*
    @PostConstruct
    public void migrateAvatars() {
        // 迁移已完成，暂时禁用
        if (true) return;
        
        // 完整保留的原有迁移逻辑
        if (userRepository.countByAvatarContaining("bottts|pixel-art|identicon") > 0) {
            return;
        }
        
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String avatar = user.getAvatar();
            if (avatar == null || 
                avatar.contains("boringavatars.com") || 
                (avatar.contains("dicebear.com") && !avatar.matches(".*(bottts|pixel-art|identicon).*"))) {
                user.setAvatar(generateNewAvatar());
                userRepository.save(user);
            }
        }
    }
    */

    public String generateNewAvatar() {
        // 使用多个国内可访问的头像API，随机选择一种
        String seed = UUID.randomUUID().toString();
        
        // 可选的API列表
        String[] apis = {
            "https://api.dicebear.com/7.x/bottts/svg?seed=" + seed, // 机器人风格
            "https://api.dicebear.com/7.x/pixel-art/svg?seed=" + seed, // 像素风格
            "https://api.dicebear.com/7.x/identicon/svg?seed=" + seed // 抽象风格
        };
        
        // 随机选择一个API
        Random random = new Random();
        return apis[random.nextInt(apis.length)];
    }

    public String uploadAvatar(MultipartFile file) throws IOException {
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只能上传图片文件");
        }

        // 生成文件名
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        String filePath = uploadPath + File.separator + fileName;
        
        // 保存文件
        File dest = new File(filePath);
        file.transferTo(dest);
        
        // 返回可访问的URL
        return "/avatars/" + fileName;
    }

    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .filter(ext -> ext.matches("(?i)\\.(jpg|jpeg|png|gif)$"))
                .orElse(".jpg");
    }

    public User registerUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("ROLE_USER");
        user.setAvatar(generateNewAvatar()); // 设置随机头像
        return userRepository.save(user);
    }

    public User createAdminUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("ROLE_ADMIN");
        user.setAvatar(generateNewAvatar()); // 设置随机头像
        return userRepository.save(user);
    }

    public User createGuestUser() {
        User user = new User();
        user.setUsername("guest");
        user.setPassword(passwordEncoder.encode("guest"));
        user.setEmail("guest@guest.com");
        user.setRole("ROLE_GUEST");
        user.setAvatar(generateNewAvatar());
        return user;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        
        // 如果用户存在但没有头像，或者头像是DiceBear的，并且数据库中没有自定义头像
        if (user.isPresent() && (user.get().getAvatar() == null || user.get().getAvatar().contains("dicebear.com"))) {
            // 修改第184-185行逻辑
            if (user.isPresent() && user.get().getAvatar() == null) {
                user.get().setAvatar(generateNewAvatar());
            }
            userRepository.save(user.get());
        }
        
        return user;
    }

    public void updateProfile(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (StringUtils.hasText(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
        }

        if (StringUtils.hasText(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 只有当头像URL不是DiceBear的时候才更新
        if (StringUtils.hasText(user.getAvatar()) && !user.getAvatar().contains("dicebear.com")) {
            existingUser.setAvatar(user.getAvatar());
        }

        userRepository.save(existingUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String keyword, User currentUser) {
        return userRepository.findByUsernameContainingOrEmailContainingAndIdNot(
            keyword, keyword, currentUser.getId());
    }

    private void createGuestUserIfNotFound() {
        Optional<User> guestUser = userRepository.findByUsername("guest");
        if (!guestUser.isPresent()) {            User user = new User();
            user.setUsername("访客");
            // 使用硬编码密码 "guest"，生产环境请修改
            user.setPassword(passwordEncoder.encode("guest")); 
            user.setEmail("guest@guest.com");
            user.setRole("ROLE_GUEST");
            user.setAvatar(generateNewAvatar()); // 为访客生成头像
            userRepository.save(user);
            System.out.println("访客账号 'guest' 已自动创建。");
        }
    }
}