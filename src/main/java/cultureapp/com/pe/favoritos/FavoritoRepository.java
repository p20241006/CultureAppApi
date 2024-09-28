package cultureapp.com.pe.favoritos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    Optional<Favorito> findByUserIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT f FROM Favorito f WHERE f.user.id = :userId AND f.isFavorite = true")
    List<Favorito> findAllByUserIdAndFavoritoTrue(@Param("userId") Integer userId);
}