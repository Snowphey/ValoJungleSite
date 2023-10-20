package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoueurRepository extends JpaRepository<Joueur, Long> {
}
