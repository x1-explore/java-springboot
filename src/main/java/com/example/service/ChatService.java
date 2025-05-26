package com.example.service;

import com.example.entity.ChatMessage;
import com.example.entity.User;
import com.example.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    public void sendMessage(ChatMessage message) {
        // 确保接收者存在
        User receiver = userService.findByUsername(message.getReceiver().getUsername())
                .orElseThrow(() -> new RuntimeException("接收者不存在"));
        message.setReceiver(receiver);
        
        // 设置消息属性
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        message.setType(ChatMessage.MessageType.CHAT);
        
        // 保存消息
        chatMessageRepository.save(message);

        // 发送给接收者
        messagingTemplate.convertAndSendToUser(
            receiver.getUsername(),
            "/queue/messages",
            message
        );
    }

    public List<ChatMessage> getChatHistory(User user1, User user2) {
        return chatMessageRepository.findChatMessagesBetweenUsers(user1, user2);
    }

    public List<User> getChatPartners(User user) {
        return chatMessageRepository.findChatPartners(user);
    }

    public void markMessagesAsRead(User sender, User receiver) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByReceiverAndReadFalse(receiver);
        for (ChatMessage message : unreadMessages) {
            if (message.getSender().equals(sender)) {
                message.setRead(true);
                chatMessageRepository.save(message);
            }
        }
    }
} 