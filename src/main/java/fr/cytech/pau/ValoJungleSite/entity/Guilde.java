package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Guilde {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String tag;

    private String lienEmbleme;

    private String couleurHex;

    @OneToMany
    private List<Joueur> membres = new ArrayList<>();

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

    public List<Joueur> getMembres() {
        return membres;
    }

    public void addMembre(Joueur membre) {
        this.membres.add(membre);
    }

    public void removeMembre(Joueur membre) {
        this.membres.remove(membre);
    }

    public void setMembres(List<Joueur> membres) {
        this.membres = membres;
    }
}
