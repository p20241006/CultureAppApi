package cultureapp.com.pe.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventTransactionHistoryRepository extends JpaRepository<EventTransactionHistory, Integer> {

    @Query("""
            SELECT history
            FROM EventTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<EventTransactionHistory> findAllScoredEvents(Pageable pageable, Integer userId);

}
