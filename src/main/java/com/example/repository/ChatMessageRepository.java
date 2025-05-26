package com.example.repository;

import com.example.entity.ChatMessage;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    @Query("SELECT m FROM ChatMessage m WHERE (m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1) ORDER BY m.timestamp")
    List<ChatMessage> findChatMessagesBetweenUsers(User user1, User user2);
    
    List<ChatMessage> findByReceiverAndReadFalse(User receiver);
    
    @Query("SELECT DISTINCT CASE WHEN m.sender = ?1 THEN m.receiver ELSE m.sender END FROM ChatMessage m WHERE m.sender = ?1 OR m.receiver = ?1")
    List<User> findChatPartners(User user);
} 