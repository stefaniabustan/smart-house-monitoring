package ro.bstefania.ds2024.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.websockets.NotificationMessage;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToUser(Long userId, NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/user-" + userId, message);
    }
}
