package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {
    List<Partie> findByInscriptionsOuvertes(boolean inscriptionsOuvertes);

    @Query("SELECT p FROM Partie p " +
            "LEFT JOIN p.modeDeJeu mj " +
            "WHERE LOWER(CAST(p.dateHeureRDV AS string)) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(CAST(p.nbJoueursMin AS string)) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(CAST(p.nbJoueursMax AS string)) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(mj.nom) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(mj.description) LIKE LOWER(concat('%', :search, '%'))")
    List<Partie> searchGames(@Param("search") String search);
}
