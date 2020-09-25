package projet.jee.transfertspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import projet.jee.transfertspring.dao.ClientRepository;
import projet.jee.transfertspring.model.Client;

@PreAuthorize("hasAuthority('ROLE_CAISSIER')")
@Controller
@RequestMapping("/parameter")
public class ParameterController {
    @Autowired
    ClientRepository clientRepository;
    /*@GetMapping("/client/{id}")
    public @ResponseBody
    Client OneClient(@PathVariable(name = "id") int destinataireId){
        return clientRepository.findById(destinataireId).get();
    }*/
    @GetMapping("/clientD/{nom}/{prenom}")
    public @ResponseBody
    Client OneClientByTelephone(@PathVariable(name = "nom") String nom,@PathVariable(name = "prenom") String prenom){
        return clientRepository.findByNomAndPrenom(nom,prenom);
    }
}
