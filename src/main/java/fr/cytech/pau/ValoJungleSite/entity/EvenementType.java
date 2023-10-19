package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
public class EvenementType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    private String lienGIF;

    @OneToMany
    private List<Evenement> evenements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLienGIF() {
        return lienGIF;
    }

    public void setLienGIF(String lienGIF) {
        this.lienGIF = lienGIF;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }
}
