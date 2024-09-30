package cultureapp.com.pe.event;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    @Query("""
            SELECT event
            FROM Event event
            WHERE event.archived = false
            AND event.shareable = true
            """)
    Page<Event> findAllDisplayableEvents(Pageable pageable, Integer userId);


    // Obtener eventos por una lista de ids de categorías
    @Query("SELECT e FROM Event e WHERE e.category.id IN :categoria_id AND e.archived=false")
    List<Event> findEventsByCategoryIds(@Param("categoria_id") List<Integer> categoria_Id);


    @Query(value = "SELECT * FROM EVENTO WHERE FECHA_INICIO >= CURRENT_DATE AND ARCHIVADO = false ORDER BY FECHA_INICIO ASC LIMIT 12", nativeQuery = true)
    List<Event> findTop12UpcomingEvents();

    // Usamos LIKE para realizar búsquedas que contengan la palabra parcial
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND e.archived = false")
    List<Event> findByTitleContaining(@Param("searchTerm") String searchTerm);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.archived = true WHERE e.start_date < :today AND e.archived = false")
    void updateArchivedStatusForPastEvents(LocalDate today);

    @Query("SELECT e FROM Event e WHERE e.start_date < :today AND e.archived = false")
    List<Event> findEventsToArchive(@Param("today") LocalDate today);

}
