package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByUsername(String username);

    @Query("SELECT u FROM Utilisateur u " +
            "LEFT JOIN u.joueur j " +
            "LEFT JOIN u.organisateur o " +
            "WHERE LOWER(u.username) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(j.nom) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(j.prenom) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(j.pseudo) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(o.discordUsername) LIKE LOWER(concat('%', :search, '%')) " +
            "OR LOWER(o.email) LIKE LOWER(concat('%', :search, '%'))")
    List<Utilisateur> searchUsers(@Param("search") String search);
}
