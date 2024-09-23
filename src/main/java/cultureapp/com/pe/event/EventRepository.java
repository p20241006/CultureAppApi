package cultureapp.com.pe.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
    @Query("SELECT e FROM Event e WHERE e.category.id IN :categoria_id")
    List<Event> findEventsByCategoryIds(@Param("categoria_id") List<Integer> categoria_Id);


    // Consulta para obtener los eventos más próximos a una fecha dada
    @Query("SELECT e FROM Event e WHERE e.start_date >= :fecha ORDER BY e.start_date ASC")
    List<Event> findProximosEventos(LocalDate fecha);

}
