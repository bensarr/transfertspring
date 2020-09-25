package projet.jee.transfertspring.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.jee.transfertspring.model.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {
    public Client findByNci(String nci);
    public Client findByTelephone(String telephone);
    public Client findByNomAndPrenom(String nom,String prenom);
    public Boolean existsByTelephone(String telephone);
    public List<Client> findAllByNciContains(String nci);
    public Page<Client> findByNciContains(String nci, Pageable pageable);
}
