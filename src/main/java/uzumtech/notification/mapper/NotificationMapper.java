package uzumtech.notification.mapper;

import org.apache.logging.log4j.util.Strings;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uzumtech.notification.dto.NotificationSendRequestDto;
import uzumtech.notification.entity.Merchant;
import uzumtech.notification.entity.Notification;
import uzumtech.notification.repository.MerchantRepository;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(source = "merchantId", target = "merchant", qualifiedByName = "merchantFromId")
    @Mapping(source = "receiver", target = "receiverInfo", qualifiedByName = "receiverToString")
    Notification toEntity(NotificationSendRequestDto dto, @Context MerchantRepository merchantRepository);

    @Named("merchantFromId")
    default Merchant mapMerchant(Long merchantId, @Context MerchantRepository   merchantRepository) {
        return merchantRepository.findById(merchantId)
            .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + merchantId));
    }

    @Named("receiverToString")
    default String receiverToString(NotificationSendRequestDto.Receiver receiver) {
        if (receiver == null) {
            return null;
        }

        if (receiver.getPhone() != null && !receiver.getPhone().isBlank()) {
            return receiver.getPhone();
        }
        if (receiver.getEmail() != null && !receiver.getEmail().isBlank()) {
            return receiver.getEmail();
        }
        if (receiver.getFirebaseTokens() != null && !receiver.getFirebaseTokens().isEmpty()) {
            return Strings.join(receiver.getFirebaseTokens(), ',');
        }

        return null; // in case all are empty
    }

}
