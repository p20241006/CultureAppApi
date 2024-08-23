package cultureapp.com.pe.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    @Query("""
            SELECT event
            FROM Event event
            WHERE event.archived = false
            AND event.shareable = true
            AND event.owner.id != :userId
            """)
    Page<Event> findAllDisplayableEvents(Pageable pageable, Integer userId);
}
