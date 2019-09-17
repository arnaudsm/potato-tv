package fr.takima.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 */

@Controller
@RequestMapping("/")
public class LibraryController {

    private final UserDAO userDAO;
    private final EpisodeDAO episodeDAO;

    public LibraryController(UserDAO userDAO, EpisodeDAO episodeDAO) {
        this.userDAO = userDAO;
        this.episodeDAO = episodeDAO;
    }

    @GetMapping
    public String afficherHomePage(){
        return "index";
    }

    @GetMapping("/{userId}")
    public String afficherPagePerso(){
        return "index";
    }

    @GetMapping("/create")
    public String afficherCreateUserPage(Model model){
        model.addAttribute("user", new User());
        return "create";
    }

    @PostMapping("/create")
    public RedirectView create_user(@ModelAttribute User user, RedirectAttributes attributes) {
        try {
            //attributes.addFlashAttribute("message", "Utilisateur ajouté avec succès");
            userDAO.save(user);
            return new RedirectView("/");
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return new RedirectView("/create");
        }
    }

    @GetMapping("/login")
    public String afficherLogUserPage(Model model){
        model.addAttribute("userLog", new User());
        return "login";
    }

    @PostMapping("/login")
    public RedirectView log_user(@ModelAttribute User user, RedirectAttributes attributes) {
        try {
            int id = userDAO.findUserId(user.getUserName());
            return new RedirectView("/" + id);
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return new RedirectView("/login");
        }
    }

   /* @PostMapping()
    private boolean add_episode(long show_id, int season_id, int episode_id, int duration_min, int user_id){
        return true;
    }

/*
  private boolean login (String user_name, String password){
    return true;
  }


  private int time_spent(int user_id){
    int total_duration = 0;
    return total_duration;
  }

/*
  @GetMapping("/")
  public String homePage(Model m) {
    m.addAttribute("users", userDAO.findAll());
    return "login";
  }

  @GetMapping("/index")
  public String addUserPage(Model m) {
    m.addAttribute("user", new User());
    return "new";
  }

  @PostMapping("/new")
  public RedirectView createNewUser(@ModelAttribute User user, RedirectAttributes attrs) {
    attrs.addFlashAttribute("message", "Utilisateur ajouté avec succès");
    userDAO.save(user);
    return new RedirectView("/");
  }

  @GetMapping("/nepisode")
  public String addEpisodePage(Model m) {
    m.addAttribute("episode", new Episode());
    return "newEpisode";
  }

  @PostMapping("/nepisode")
  public RedirectView addNewEpisode(@ModelAttribute Episode episode, RedirectAttributes attrs) {
    attrs.addFlashAttribute("message", "Episode ajouté avec succès");
    episodeDAO.save(episode);
    return new RedirectView("/");
  }
*/

}
