package fr.cytech.pau.ValoJungleSite.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.sql.Date;
import java.util.Set;

@Entity
public class Evenement {
    @Id
    private Long id;
    private int nbJoueurMin;
    private int nbJoueurMax;
    private Date dateHeureRDV;
    private boolean inscriptionsOuvertes;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Date getDateHeureRDV() {
        return dateHeureRDV;
    }

    public void setDateHeureRDV(Date dateHeureRDV) {
        this.dateHeureRDV = dateHeureRDV;
    }

    public boolean isInscriptionsOuvertes() {
        return inscriptionsOuvertes;
    }

    public void setInscriptionsOuvertes(boolean inscriptionsOuvertes) {
        this.inscriptionsOuvertes = inscriptionsOuvertes;
    }

    @OneToMany
    Set<Joueur> participant;

    @OneToOne
    private EvenementType evenementType;

}
