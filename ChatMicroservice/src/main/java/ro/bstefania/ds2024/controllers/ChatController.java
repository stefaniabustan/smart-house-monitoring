package ro.bstefania.ds2024.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ro.bstefania.ds2024.entities.Message;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //handle messages from users
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Message messageDto) {

        System.out.println(messageDto.toString());
        String recipient = messageDto.getIsGroupChat() ? null : messageDto.getRecipient();
        Message chatMessage = new Message(messageDto.getSender(),recipient,messageDto.getContent(),LocalDateTime.now(),
                messageDto.getIsGroupChat());
        if (chatMessage.getSender() == null || chatMessage.getRecipient() == null) {
            throw new IllegalArgumentException("Sender and recipient must not be null.");
        }

        if ("All Users".equals(chatMessage.getRecipient())) {
            messagingTemplate.convertAndSend("/topic/chat/all", chatMessage);
        } else {
            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRecipient(), chatMessage);
        }
    }

    //handle read notifications
    @MessageMapping("/readNotification")
    public void handleRead(@Payload Message chatMessage) {
        if (chatMessage.getRecipient() == null || chatMessage.getSender() == null) {
            throw new IllegalArgumentException("Sender and recipient must not be null.");
        }

        Message readMessage = new Message();
        readMessage.setSender(chatMessage.getSender());
        readMessage.setRecipient(chatMessage.getRecipient());
        readMessage.setIsRead(true);
        readMessage.setTimestamp(LocalDateTime.now());
        readMessage.getReadBy().add(chatMessage.getRecipient());

        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getSender(), readMessage);
    }


    @MessageMapping("/typing")
    public void handleTyping(@Payload Message chatMessage) {
        if ("All Users".equals(chatMessage.getRecipient())){
            Message typingMessage = new Message();
            typingMessage.setSender(chatMessage.getSender());
            typingMessage.setContent(chatMessage.getSender() + " is typing...");
            typingMessage.setIsGroupChat(true);

            messagingTemplate.convertAndSend("/topic/chat/typing/all", typingMessage);
        } else if (chatMessage.getRecipient() != null && !chatMessage.getRecipient().equals(chatMessage.getSender())) {
            Message typingMessage = new Message();
            typingMessage.setSender(chatMessage.getSender());
            typingMessage.setContent(chatMessage.getSender() + " is typing...");

            messagingTemplate.convertAndSend("/topic/chat/typing/" + chatMessage.getRecipient(), typingMessage);
        }
    }
}