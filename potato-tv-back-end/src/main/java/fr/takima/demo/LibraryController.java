package fr.takima.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /*@PostMapping
    public boolean create_user(@RequestParam String user_name, @RequestParam(name = "password_hash") String password) {
        try {
            new RedirectView("/index");
            System.out.println("avant");
            User user = new User(user_name, password);
            System.out.println("fait");
            return true;
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return false;
        }
    }
/*
  private boolean login (String user_name, String password){
    return true;
  }

  private boolean add_episode(long show_id, int season_id, int episode_id, int duration_min, int user_id){
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
