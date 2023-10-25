package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.ModeDeJeu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeDeJeuRepository extends JpaRepository<ModeDeJeu, Long> {
    @Query("SELECT mj FROM ModeDeJeu mj " +
            "WHERE LOWER(mj.nom) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(mj.description) LIKE LOWER(concat('%', :search, '%'))")
    List<ModeDeJeu> searchGamemodes(@Param("search") String search);
}
