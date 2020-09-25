package projet.jee.transfertspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Client extends Personne{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nci;
    @JsonIgnore
    @OneToMany(mappedBy = "clientEmetteur")
    private List<Transfert> envois;
    @JsonIgnore
    @OneToMany(mappedBy = "clientRecepteur")
    private List<Transfert> retraits;

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNci() {
        return nci;
    }

    public void setNci(String nci) {
        this.nci = nci;
    }

    public List<Transfert> getEnvois() {
        return envois;
    }

    public void setEnvois(List<Transfert> envois) {
        this.envois = envois;
    }

    public List<Transfert> getRetraits() {
        return retraits;
    }

    public void setRetraits(List<Transfert> retraits) {
        this.retraits = retraits;
    }
}
