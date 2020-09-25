package projet.jee.transfertspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projet.jee.transfertspring.dao.*;
import projet.jee.transfertspring.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {
    //Destination upload
    private static String UPLOADED_IMAGES = "C://Users//bensa//Desktop//uploadFileSpring//";

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CaissierRepository caissierRepository;
    @Autowired
    DepotRepository depotRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/index")
    public String Index(Model model,
                        @RequestParam(name="pagecaissier",defaultValue = "0") int pagecaissier,
                        @RequestParam(name="sizecaissier",defaultValue = "5")int sizecaissier,
                        @RequestParam(name="pageclient",defaultValue = "0") int pageclient,
                        @RequestParam(name="sizeclient",defaultValue = "5")int sizeclient,
                        @RequestParam(name="search_code",defaultValue = "")String search_code,
                        @RequestParam(name="search_nci",defaultValue = "")String search_nci){
        Client client = new Client();
        Caissier caissier=new Caissier();
        Depot depot=new Depot();
        Page<Caissier> caissiers=caissierRepository
                .findByCodeContains(search_code,PageRequest.of(pagecaissier,sizecaissier));
        Page<Client> clients= clientRepository.
                findByNciContains(search_nci,PageRequest.of(pageclient,sizeclient));
        model.addAttribute("depot", depot);
        model.addAttribute("caissier", caissier);
        model.addAttribute("client", client);
        model.addAttribute("caissiers", caissiers);
        model.addAttribute("clients",clients);

        model.addAttribute("currentPageCaissier",pagecaissier);
        model.addAttribute("sizePageCaissier",sizecaissier);
        model.addAttribute("currentPageClient",pageclient);
        model.addAttribute("sizePageClient",sizeclient);

        model.addAttribute("pageCaissier",new int[caissiers.getTotalPages()]);
        model.addAttribute("pageClient",new int[clients.getTotalPages()]);
        model.addAttribute("search_code",search_code);
        model.addAttribute("search_nci",search_nci);
        return "admin";
    }

    @PostMapping("/caissier/add")
    public String addcaissier(@ModelAttribute("caissier") Caissier caissier) {
        byte[] bytes = {};
        Path path = null;
        try {
            if(!caissier.getFiles()[0].getName().equals("")){
                MultipartFile file = caissier.getFiles()[0];
                bytes = file.getBytes();
                path = Paths.get(UPLOADED_IMAGES + file.getOriginalFilename());
                caissier.setPhoto(file.getOriginalFilename());
            }
            else{
                caissier.setPhoto("default.jpeg");
            }
            //ROLES
            caissier.setRoles(roleRepository.findByLibelle("ROLE_CAISSIER"));
            //password
            caissier.setPassword(encoder.encode("passer123"));
            //contrat
            int max=caissierRepository.findAll().size()+1;
            caissier.setNumContrat("ctr_"+max);
            //code
            caissier.setCode(this.randomCode(6));
            Caissier caissierP=caissierRepository.save(caissier);
            //Articles
            if(caissier.getTemps().length>0)
            {
                List<Article> articles=new ArrayList<>();
                articles.addAll(createArticles(caissierP,caissier.getTemps()));
                if(!articles.isEmpty())
                    caissierP.setArticles(articles);
            }
            //Depot
            caissierP.setSolde(100000.0);
            caissierP.setDepots(doDepot(caissierP,100000.0));
            caissierRepository.flush();
            if(bytes.length != 0) {
                Files.write(path, bytes);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/index";
    }


    @PostMapping("/client/add")
    public String addclient(@ModelAttribute("client") Client client) {
        try {
            clientRepository.save(client);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/index";
    }
    @PostMapping("/client/remove")
    public String deleteclient(int clientId) {
        Client c = clientRepository.getOne(clientId);
        clientRepository.delete(c);
        return "redirect:/admin/index";
    }
    @PostMapping("/caissier/depot")
    public String depotCaissier(@ModelAttribute("depot") Depot depot) {
        try {
            Caissier caissier=caissierRepository.findByCode(depot.getCaissier().getCode());
            caissier.setSolde(caissier.getSolde()+depot.getMontant());
            caissier.setDepots(doDepot(caissier,depot.getMontant()));

            caissierRepository.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/index";
    }

    @GetMapping("/caissier/{id}")
    public @ResponseBody
    Caissier OneCaissier(@PathVariable(name = "id") int caissierId){
        return caissierRepository.findById(caissierId).get();
    }
    @PostMapping("/caissier/remove")
    public String deletecaissier(int caissierId) {
        Caissier c = caissierRepository.getOne(caissierId);
        depotRepository.deleteAll(c.getDepots());
        articleRepository.deleteAll(c.getArticles());
        caissierRepository.delete(c);
        return "redirect:/admin/index";
    }
    //******************CLIENT FIN*********************
    private List<Article> createArticles(Caissier caissier,String [] temps)
    {
        List<Article> articles=new ArrayList<>();
        int i=0;
        for(String d:temps)
        {
            if(!d.isEmpty())
                try {
                    Article a = new Article();
                    i++;
                    a.setLibelle("Article " + i);
                    a.setDescription(d);
                    a.setCaissier(caissier);
                    Article ac=articleRepository.save(a);
                    articles.add(ac);
                }catch(Exception e){
                    e.printStackTrace();
                }
            else
                return null;
        }
        return articles;
    }

    private List<Depot> doDepot(Caissier caissier,double montant)
    {
        List<Depot> depots=new ArrayList<>();
        try {
            Depot depot=new Depot();
            depot.setMontant(montant);
            depot.setDate(LocalDateTime.now());
            depot.setCaissier(caissier);
            Depot depotc=depotRepository.save(depot);
            depots.add(depotc);
        }catch(Exception e){
            e.printStackTrace();
        }
        return depots;
    }

    @RequestMapping(value = "image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
        System.out.println("called");
        File serverFile = new File("C://Users//bensa//Desktop//uploadFileSpring//" + imageName + ".jpg");
        return Files.readAllBytes(serverFile.toPath());
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




}
