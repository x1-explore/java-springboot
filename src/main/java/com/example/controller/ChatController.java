package com.example.controller;

import com.example.entity.ChatMessage;
import com.example.entity.User;
import com.example.service.ChatService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String chatPage(Model model, @AuthenticationPrincipal User currentUser) {
        List<User> chatPartners = chatService.getChatPartners(currentUser);
        model.addAttribute("chatPartners", chatPartners);
        return "chat/index";
    }

    @GetMapping("/{username}")
    public String chatWithUser(@PathVariable String username, Model model, @AuthenticationPrincipal User currentUser) {
        User partner = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        List<ChatMessage> chatHistory = chatService.getChatHistory(currentUser, partner);
        model.addAttribute("chatHistory", chatHistory);
        model.addAttribute("partner", partner);
        return "chat/chat";
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage message, @AuthenticationPrincipal User sender) {
        message.setSender(sender);
        chatService.sendMessage(message);
    }

    @SubscribeMapping("/user/queue/messages")
    public List<ChatMessage> subscribeToMessages(@AuthenticationPrincipal User user) {
        return chatService.getChatHistory(user, user);
    }

    @GetMapping("/api/chat/history/{username}")
    @ResponseBody
    public List<ChatMessage> getChatHistoryApi(@PathVariable String username, @AuthenticationPrincipal User currentUser) {
        User partner = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return chatService.getChatHistory(currentUser, partner);
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<User> searchUsers(@RequestParam String keyword, @AuthenticationPrincipal User currentUser) {
        return userService.searchUsers(keyword, currentUser);
    }
} 