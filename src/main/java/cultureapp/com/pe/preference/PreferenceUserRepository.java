package cultureapp.com.pe.preference;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PreferenceUserRepository extends JpaRepository<PreferenceUser, Integer> {

    @Query("""
            SELECT preference
            FROM PreferenceUser preference
            WHERE preference.user.id = :userId
            """)
    Page<PreferenceUser> findAllScoredEvents(Pageable pageable, Integer userId);

    // Buscar una relación de preferencia por usuario y evento
    Optional<PreferenceUser> findByUserAndEvent(User user, Event event);

}
