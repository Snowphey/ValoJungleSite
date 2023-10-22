package fr.cytech.pau.ValoJungleSite.repository;

import fr.cytech.pau.ValoJungleSite.entity.Partie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartieRepository extends JpaRepository<Partie, Long> {
    List<Partie> findByInscriptionsOuvertes(boolean inscriptionsOuvertes);
}
