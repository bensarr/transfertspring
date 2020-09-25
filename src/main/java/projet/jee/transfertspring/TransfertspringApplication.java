package projet.jee.transfertspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projet.jee.transfertspring.dao.RoleRepository;
import projet.jee.transfertspring.dao.UtilisateurRepository;
import projet.jee.transfertspring.model.Role;
import projet.jee.transfertspring.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TransfertspringApplication implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public static void main(String[] args) {
        SpringApplication.run(TransfertspringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*Role r=new Role();
        Role r1=new Role();
        r.setLibelle("ROLE_SUPERADMIN");
        r1.setLibelle("ROLE_CAISSIER");
        roleRepository.save(r);
        roleRepository.save(r1);
        Utilisateur u=new Utilisateur();
        u.setEmail("bensarr37@gmail.com");
        u.setUsername("bensarr37");
        u.setNom("SARR");
        u.setPrenom("El Hadji Hady");
        u.setTelephone("781456692");
        u.setPassword(encoder.encode("brandao37"));
        utilisateurRepository.save(u);*/
        System.out.println("***************"+utilisateurRepository.findAll().size()+"**********************");
    }
}
