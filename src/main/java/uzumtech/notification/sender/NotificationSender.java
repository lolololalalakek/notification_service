package uzumtech.notification.sender;

import uzumtech.notification.constant.enums.NotificationType;
import uzumtech.notification.dto.NotificationSendRequestDto;
import uzumtech.notification.dto.NotificationSendResponseDto;

public interface NotificationSender {
    NotificationSendResponseDto sendNotification(NotificationSendRequestDto notification);
    NotificationType getNotificationType();
}