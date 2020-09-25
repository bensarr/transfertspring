package projet.jee.transfertspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projet.jee.transfertspring.dao.*;
import projet.jee.transfertspring.model.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Random;

@PreAuthorize("hasAuthority('ROLE_CAISSIER')")
@Controller
@RequestMapping("/caissier")
public class CaissierController {

    @Autowired
    CaissierRepository caissierRepository;
    @Autowired
    TransfertRepository envoiRepository;
    @Autowired
    DepotRepository depotRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    private Caissier caissier;

    @GetMapping("/index")
    public String Index(Model model,Principal principal,
                        @RequestParam(name="code",defaultValue = "")String code,
                        @RequestParam(name="title",defaultValue = "")String title,
                        @RequestParam(name="message",defaultValue = "")String message,
                        @RequestParam(name="step",defaultValue = "0")int step,
                        @RequestParam(name="search_nci",defaultValue = "")String search_nci,
                        @RequestParam(name="search_code",defaultValue = "")String search_code,
                        @RequestParam(name="pageenvoi",defaultValue = "0") int pageenvoi,
                        @RequestParam(name="sizeenvoi",defaultValue = "5")int sizeenvoi,
                        @RequestParam(name="pageretrait",defaultValue = "0") int pageretrait,
                        @RequestParam(name="sizeretrait",defaultValue = "5")int sizeretrait,
                        @RequestParam(name="search_envoi_code",defaultValue = "")String search_envoi_code,
                        @RequestParam(name="search_retrait_code",defaultValue = "")String search_retrait_code){

        this.caissier= caissierRepository.findByUsername(principal.getName());
        model.addAttribute("caissier", caissier);
        if(!this.caissier.isChanged())
            return "passwordreset";

        Page<Transfert> envois=envoiRepository
                .findByCodeContainsAndCaissierEmetteur(search_envoi_code,this.caissier, PageRequest.of(pageenvoi,sizeenvoi));
        Page<Transfert> retraits=envoiRepository
                .findByCodeContainsAndCaissierRecepteur(search_retrait_code,this.caissier,PageRequest.of(pageretrait,sizeretrait));
        Transfert envoi=new Transfert();
        Depot depot= new Depot();
        model.addAttribute("depot", depot);
        model.addAttribute("envoi", envoi);
        model.addAttribute("envois", envois);
        model.addAttribute("retraits",retraits);

        model.addAttribute("currentPageEnvoi",pageenvoi);
        model.addAttribute("sizePageEnvoi",sizeenvoi);
        model.addAttribute("currentPageRetrait",pageretrait);
        model.addAttribute("sizePageRetrait",sizeretrait);

        model.addAttribute("pageEnvoi",new int[envois.getTotalPages()]);
        model.addAttribute("pageRetrait",new int[retraits.getTotalPages()]);
        model.addAttribute("search_envoi_code",search_envoi_code);
        model.addAttribute("search_retrait_code",search_retrait_code);

        //Recherche NCI
        if(!search_nci.equals("")) {
            Client clientsearch = clientRepository.
                    findByNci(search_nci);

            model.addAttribute("clientsearch", clientsearch);
            model.addAttribute("search_nci", search_nci);
        }
        //Recherche CODE
        if(!search_code.equals("")) {
            Transfert envoisearch = envoiRepository.
                    findByCode(search_code);

            model.addAttribute("envoisearch", envoisearch);
            model.addAttribute("search_code", search_code);
        }
        if(!title.equals("") &&!message.equals("") && step!=0)
        {
            model.addAttribute("title", new String(title));
            model.addAttribute("message", message);
            model.addAttribute("code", code);
            model.addAttribute("step", step);
        }
        return "caissier";
    }
    @PostMapping("/envoi")
    public String addenvoi(@ModelAttribute("envoi") Transfert envoi,Model model) {

        String title="";
        String message="";
        String code="";
        int step=0;
        /* Condition Client Récepteur*/
        boolean ok=clientRepository.existsByTelephone(envoi.getClientRecepteur().getTelephone());
        if(!ok)
        {
            title="Notification Erreur";
            message="Ces informations ne correspond pas à un Client Inscrit dans le système!!!";
            step=-1;
        }else
        {
            try
            {
                Client clientR=clientRepository.findByTelephone(envoi.getClientRecepteur().getTelephone());
                Client client=clientRepository.findByNci(envoi.getClientEmetteur().getNci());
                envoi.setCode(randomCode(10));
                envoi.setClientEmetteur(client);
                envoi.setClientRecepteur(clientR);
                envoi.setMontant(envoi.getMontant()*100/105);
                envoi.setFrais(envoi.getMontant()*15/100);
                envoi.setCaissierEmetteur(this.caissier);
                repartirEnvoi(envoi.getMontant());
                envoi.setDate(LocalDateTime.now());
                envoi.setEtat(false);
                envoiRepository.save(envoi);
                code=envoi.getCode();
                title="Notification succés";
                message="Envoi Effectué avec succés!!!";
                step=1;

                model.addAttribute("code",code);
            }
            catch(Exception e){

                title="Notification Erreur";
                message="Ce Numero de téléphone ne correspond à aucun Client";
                step=-1;
                e.printStackTrace();
            }
            code=envoi.getCode();
            title="Notification succés";
            message="Envoi Effectué avec succés";
            step=1;
        }
        /**/

        model.addAttribute("title",title);
        model.addAttribute("message",message);
        model.addAttribute("step",step);
        return "redirect:/caissier/index?title="+title+"&message="+message+"&step="+step+"&code="+code;
    }
    @PostMapping("/retrait")
    public String addretrait(@ModelAttribute("envoi") Transfert envoi,Model model) {
        String title="";
        String message="";
        String code="";
        int step=0;
        Transfert e=envoiRepository.findByCode(envoi.getCode());
        return "redirect:/caissier/index?title="+title+"&message="+message+"&step="+step+"&code="+code;
    }
    @PostMapping("/edit/password")
    public String editPassword(String password,String confirm,Model model) {
        if(!(password.compareTo(confirm)==0))
        {
            model.addAttribute("error","Mots de Pass non conforme");
            return "passwordreset";
        }
        else
            try {
                this.caissier.setPassword(encoder.encode(password));
                this.caissier.setChanged(true);
                caissierRepository.save(this.caissier);
            }catch(Exception e){
                e.printStackTrace();
            }
        return "redirect:/caissier/index";
    }
    public String randomCode(int length) {
        Random random = new Random();
        final int alphabetLength = 'Z' - 'A' + 1;
        StringBuilder result = new StringBuilder(length);
        while (result.length() < length) {
            final char charCaseBit = (char) (random.nextInt(2) << 5);
            result.append((char) (charCaseBit | ('A' + random.nextInt(alphabetLength))));
        }
        return result.toString();
    }
    private void repartirEnvoi(double montant)
    {
        double frais=montant*0.05;
        try {
            this.caissier.setSolde(this.caissier.getSolde()-montant-frais*0.85);
            caissierRepository.save(this.caissier);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void repartirRetrait(double montant)
    {
        double frais=montant*0.05;
        try {
            this.caissier.setSolde(this.caissier.getSolde()+montant+frais*0.15);
            caissierRepository.save(this.caissier);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
