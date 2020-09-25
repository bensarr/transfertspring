package projet.jee.transfertspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.jee.transfertspring.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Integer> {
}
