package projet.jee.transfertspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.jee.transfertspring.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    public List<Role> findByLibelle(String libelle);
}
