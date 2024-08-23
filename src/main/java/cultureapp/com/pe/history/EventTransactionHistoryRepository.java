package cultureapp.com.pe.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventTransactionHistoryRepository extends JpaRepository<EventTransactionHistory, Integer> {
    @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM EventTransactionHistory eventTransactionHistory
            WHERE eventTransactionHistory.user.id = :userId
            AND eventTransactionHistory.event.id = :eventId
            AND eventTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(@Param("eventId") Integer eventId, @Param("userId") Integer userId);

    @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM EventTransactionHistory eventTransactionHistory
            WHERE eventTransactionHistory.event.id = :eventId
            AND eventTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowed(@Param("eventId") Integer eventId);

    @Query("""
            SELECT transaction
            FROM EventTransactionHistory  transaction
            WHERE transaction.user.id = :userId
            AND transaction.event.id = :eventId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<EventTransactionHistory> findByEventIdAndUserId(@Param("eventId") Integer eventId, @Param("userId") Integer userId);

    @Query("""
            SELECT transaction
            FROM EventTransactionHistory  transaction
            WHERE transaction.event.owner.id = :userId
            AND transaction.event.id = :eventId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<EventTransactionHistory> findByEventIdAndOwnerId(@Param("eventId") Integer eventId, @Param("userId") Integer userId);

    @Query("""
            SELECT history
            FROM EventTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<EventTransactionHistory> findAllBorrowedEvents(Pageable pageable, Integer userId);
    @Query("""
            SELECT history
            FROM EventTransactionHistory history
            WHERE history.event.owner.id = :userId
            """)
    Page<EventTransactionHistory> findAllReturnedEvents(Pageable pageable, Integer userId);
}
