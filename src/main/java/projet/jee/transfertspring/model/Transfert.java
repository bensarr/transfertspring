package projet.jee.transfertspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Transfert extends Operation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private Double frais;
    @ManyToOne
    @JoinColumn(name = "clientRecepteur_id")
    private Client clientRecepteur;

    @ManyToOne
    @JoinColumn(name = "caissierRecepteur_id")
    private Caissier caissierRecepteur;

    private boolean etat;
    @ManyToOne
    @JoinColumn(name = "clientEmetteur_id")
    private Client clientEmetteur;

    @ManyToOne
    @JoinColumn(name = "caissierEmetteur_id")
    private Caissier caissierEmetteur;



    public Transfert() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getFrais() {
        return frais;
    }

    public void setFrais(Double frais) {
        this.frais = frais;
    }

    public Client getClientRecepteur() {
        return clientRecepteur;
    }

    public void setClientRecepteur(Client clientRecepteur) {
        this.clientRecepteur = clientRecepteur;
    }

    public Caissier getCaissierRecepteur() {
        return caissierRecepteur;
    }

    public void setCaissierRecepteur(Caissier caissierRecepteur) {
        this.caissierRecepteur = caissierRecepteur;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public Client getClientEmetteur() {
        return clientEmetteur;
    }

    public void setClientEmetteur(Client clientEmetteur) {
        this.clientEmetteur = clientEmetteur;
    }

    public Caissier getCaissierEmetteur() {
        return caissierEmetteur;
    }

    public void setCaissierEmetteur(Caissier caissierEmetteur) {
        this.caissierEmetteur = caissierEmetteur;
    }
}
