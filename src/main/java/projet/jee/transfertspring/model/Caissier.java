package projet.jee.transfertspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Caissier extends Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50)
    private String code;
    @Column(length = 6)
    private String numContrat;

    @Column(length = 50)
    private String photo;
    @Transient
    private MultipartFile[] files;

    @Transient
    private String[] temps;

    private Double solde;
    @JsonIgnore
    @OneToMany(mappedBy = "caissierEmetteur")
    private List<Transfert> envois;
    @JsonIgnore
    @OneToMany(mappedBy = "caissierRecepteur")
    private List<Transfert> retraits;

    @JsonIgnore
    @OneToMany(mappedBy = "caissier")
    private List<Depot> depots;
    @JsonIgnore
    @OneToMany(mappedBy = "caissier")
    private List<Article> articles;

    public Caissier() {
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

    public String getNumContrat() {
        return numContrat;
    }

    public void setNumContrat(String numContrat) {
        this.numContrat = numContrat;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
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

    public List<Depot> getDepots() {
        return depots;
    }

    public void setDepots(List<Depot> depots) {
        this.depots = depots;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String[] getTemps() {
        return temps;
    }

    public void setTemps(String[] temps) {
        this.temps = temps;
    }
}

