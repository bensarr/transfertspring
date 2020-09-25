package projet.jee.transfertspring.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.jee.transfertspring.model.Caissier;

@Repository
public interface CaissierRepository extends JpaRepository<Caissier,Integer> {
    public Caissier findByCode(String code);
    public Caissier findByUsername(String userName);
    public Page<Caissier> findByCodeContains(String code, Pageable pageable);
}
