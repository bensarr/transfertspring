package projet.jee.transfertspring.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.jee.transfertspring.model.Caissier;
import projet.jee.transfertspring.model.Transfert;

@Repository
public interface TransfertRepository extends JpaRepository<Transfert,Integer> {
    public Transfert findByCode(String code);
    public Page<Transfert> findByCodeContainsAndCaissierEmetteur(String code, Caissier caissier, Pageable pageable);
    public Page<Transfert> findByCodeContainsAndCaissierRecepteur(String code, Caissier caissier, Pageable pageable);
}
