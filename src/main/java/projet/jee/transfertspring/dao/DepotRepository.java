package projet.jee.transfertspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.jee.transfertspring.model.Depot;

@Repository
public interface DepotRepository extends JpaRepository<Depot,Integer> {
}
