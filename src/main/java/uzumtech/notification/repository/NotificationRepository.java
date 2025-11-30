package com.example.notification.repository;

import com.example.notification.entity.Notification;
import com.example.notification.entity.NotificationStatus;
import com.example.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>,
        JpaSpecificationExecutor<Notification> {

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByStatusOrderByCreatedAtAsc(NotificationStatus status);

    List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status);

    @Query("""
            SELECT n FROM Notification n
            WHERE n.status = :status
            ORDER BY n.createdAt ASC
            """)
    List<Notification> findForRetry(NotificationStatus status);

    @Modifying
    @Query("""
            UPDATE Notification n
            SET n.status = :status
            WHERE n.id = :id
            """)
    void updateStatus(Long id, NotificationStatus status);
}

