package uzumtech.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzumtech.notification.constant.enums.NotificationType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationSendRequestDto {
    private String title;
    private String body;
    private NotificationType type;
    private Long merchantId;
    private Receiver receiver;

    @Getter
    public static class Receiver {
        private String phone;
        private String email;
        private List<String> firebaseTokens;
    }
}