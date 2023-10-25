package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.Guilde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuildeRepository extends JpaRepository<Guilde, Long> {
    @Query("SELECT g FROM Guilde g " +
            "WHERE LOWER(g.nom) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(g.tag) LIKE LOWER(concat('%', :search, '%'))")
    List<Guilde> searchGuilds(@Param("search") String search);
}
