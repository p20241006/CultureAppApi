package cultureapp.com.pe.favoritos;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    Optional<Favorito> findByUserIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT f FROM Favorito f " +
            "JOIN f.event e " +
            "WHERE f.user.id = :userId " +
            "AND f.isFavorite = true " +
            "AND e.archived = false")
    List<Favorito> findAllByUserIdAndFavoritoTrue(@Param("userId") Integer userId);

    // Nuevo metodo para eliminar favoritos por ID de evento
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorito f WHERE f.event.id = :eventId")
    void deleteByEventId(@Param("eventId") Integer eventId);

}