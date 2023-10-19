package fr.cytech.pau.ValoJungleSite.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nbJoueurMin;

    private int nbJoueurMax;

    private LocalDateTime dateHeureRDV;

    private boolean inscriptionsOuvertes;

    @ManyToOne
    private EvenementType type;

    @OneToMany
    private List<Joueur> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNbJoueurMin() {
        return nbJoueurMin;
    }

    public void setNbJoueurMin(int nbJoueurMin) {
        this.nbJoueurMin = nbJoueurMin;
    }

    public int getNbJoueurMax() {
        return nbJoueurMax;
    }

    public void setNbJoueurMax(int nbJoueurMax) {
        this.nbJoueurMax = nbJoueurMax;
    }

    public LocalDateTime getDateHeureRDV() {
        return dateHeureRDV;
    }

    public void setDateHeureRDV(LocalDateTime dateHeureRDV) {
        this.dateHeureRDV = dateHeureRDV;
    }

    public boolean isInscriptionsOuvertes() {
        return inscriptionsOuvertes;
    }

    public void setInscriptionsOuvertes(boolean inscriptionsOuvertes) {
        this.inscriptionsOuvertes = inscriptionsOuvertes;
    }

    public EvenementType getType() {
        return type;
    }

    public void setType(EvenementType type) {
        this.type = type;
    }

    public List<Joueur> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Joueur> participants) {
        this.participants = participants;
    }
}
