package fr.cytech.pau.ValoJungleSite.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nbJoueursMin;

    private int nbJoueursMax;

    private LocalDateTime dateHeureRDV;

    private boolean inscriptionsOuvertes = true;

    @ManyToOne
    private ModeDeJeu modeDeJeu;

    @ManyToMany(mappedBy = "parties")
    private List<Joueur> participants = new ArrayList<>();

    @ManyToOne
    private Organisateur createur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNbJoueursMin() {
        return nbJoueursMin;
    }

    public void setNbJoueursMin(int nbJoueursMin) {
        this.nbJoueursMin = nbJoueursMin;
    }

    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    public void setNbJoueursMax(int nbJoueursMax) {
        this.nbJoueursMax = nbJoueursMax;
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

    public ModeDeJeu getModeDeJeu() {
        return modeDeJeu;
    }

    public void setModeDeJeu(ModeDeJeu modeDeJeu) {
        this.modeDeJeu = modeDeJeu;
    }

    public List<Joueur> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Joueur> participants) {
        this.participants = participants;
    }

    public void addParticipant(Joueur participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(Joueur participant) {
        this.participants.remove(participant);
    }

    public Organisateur getCreateur() {
        return createur;
    }

    public void setCreateur(Organisateur createur) {
        this.createur = createur;
    }
}
