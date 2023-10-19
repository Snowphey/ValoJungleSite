package fr.cytech.pau.ValoJungleSite.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.util.Set;

@Entity
public class Evenement {
    @Id
    private Long id;
    private int nbJoueurMin;
    private int nbJoueurMax;
    private Date dateHeureRDV;
    private String nom;
    private String lienGIF;
    private EvenementType type;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLienGIF() {
        return lienGIF;
    }

    public void setLienGIF(String lienGIF) {
        this.lienGIF = lienGIF;
    }

    public EvenementType getType() {
        return type;
    }

    public void setType(EvenementType type) {
        this.type = type;
    }

    @OneToMany
    Set<Joueur> participant;

}
