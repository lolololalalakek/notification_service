package uzumtech.notification.sender;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uzumtech.notification.constant.enums.NotificationType;
import uzumtech.notification.dto.NotificationSendRequestDto;
import uzumtech.notification.dto.NotificationSendResponseDto;
import uzumtech.notification.entity.Notification;
import uzumtech.notification.mapper.NotificationMapper;
import uzumtech.notification.repository.MerchantRepository;
import uzumtech.notification.repository.NotificationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationSender implements NotificationSender {

    private final NotificationRepository notificationRepository;
    private final MerchantRepository merchantRepository;
    private final NotificationMapper notificationMapper;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public NotificationSendResponseDto sendNotification(NotificationSendRequestDto notification) {
        Notification entity = notificationMapper.toEntity(
            notification, merchantRepository);



    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PUSH;
    }

    private void sendPush(NotificationSendRequestDto notificationDto) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                    .setTitle(notificationDto.getTitle())
                    .setBody(notificationDto.getBody())
                    .build())
                .addAllTokens(notificationDto.getReceiver().getFirebaseTokens())
                .build();

            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            List<SendResponse> responses = response.getResponses();

            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    log.error("Failed to send to token {}: {}", notificationDto.getReceiver().getFirebaseTokens().get(i),
                        responses.get(i).getException().getMessage());
                }
            }
            log.info("Firebase notification sent to {} devices, {} succeeded, {} failed",
                notificationDto.getReceiver().getFirebaseTokens().size(),
                response.getSuccessCount(),
                response.getFailureCount()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Firebase notification multicast sending error: {}", e.getMessage(), e);
        }
    }
}
