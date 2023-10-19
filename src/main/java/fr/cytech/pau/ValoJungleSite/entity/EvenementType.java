package fr.cytech.pau.ValoJungleSite.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class EvenementType {

    @Id
    private Long id;
    private String nom;
    private String description;
    private String lienGif;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getLienGif() {
        return lienGif;
    }

    public void setLienGif(String lienGif) {
        this.lienGif = lienGif;
    }

    //Obtenir la liste d'un type d'évenement (ex: voir tous les LG disponibles)
    @OneToMany
    Set<Evenement> listeEvenementType;

}
