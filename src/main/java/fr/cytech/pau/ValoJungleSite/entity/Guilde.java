package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Guilde {
    @Id
    private Long id;
    private String nom;
    private String tag;
    private String lienEmbleme;
    private String couleurHex;


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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLienEmbleme() {
        return lienEmbleme;
    }

    public void setLienEmbleme(String lienEmbleme) {
        this.lienEmbleme = lienEmbleme;
    }

    public String getCouleurHex() {
        return couleurHex;
    }

    public void setCouleurHex(String couleurHex) {
        this.couleurHex = couleurHex;
    }

    @OneToMany
    Set<Joueur> membreGuilde;


}
