package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ModeDeJeu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    private String lienGIF;

    @OneToMany(mappedBy = "modeDeJeu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Partie> parties;

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

    public List<Partie> getParties() {
        return parties;
    }

    public void setParties(List<Partie> parties) {
        this.parties = parties;
    }
}
