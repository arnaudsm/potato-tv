package fr.takima.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.List;

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
    public String displayHomePage() {
        return "index";
    }

    @GetMapping("/{userId}")
    public String displayPagePerso() {
        return "index";
    }

    @GetMapping("/create")
    public String displayCreateUserPage(Model model) {
        model.addAttribute("user", new User());
        return "create";
    }

    @PostMapping("/create")
    public RedirectView createUser(@ModelAttribute User user) {
        try {
            userDAO.save(user);
            return new RedirectView("/" + userDAO.userId(user.getUserName()));
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return new RedirectView("/create");
        }
    }

    @GetMapping("/login")
    public String displayLogUserPage(Model model) {
        model.addAttribute("userLog", new User());
        return "login";
    }

    @PostMapping("/login")
    public RedirectView log_user(@ModelAttribute User user) {
        try {
            long id = userDAO.userId(user.getUserName());
            //if(user.getPassword().equals(userDAO.userPassword(id)))
                return new RedirectView("/" + id);
            //else
              //  return new RedirectView("/login");
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return new RedirectView("/create");
        }
    }

    @GetMapping("/exit")
    public RedirectView signOut() {
        return new RedirectView("/");
    }

    @GetMapping("/{userId}/library")
    public String showShows(@PathVariable("userID") long id, Model model) {
        List<String> seriesPerso = episodeDAO.mySeries(id);
        String viewedEpisodes = new String();
        for (String episode : seriesPerso) {
            viewedEpisodes.concat(episode);
        }
        model.addAttribute("series", seriesPerso);
        return viewedEpisodes;
    }

    @PostMapping("/addEpisode")
    public boolean addEpisode(long showId, int seasonId, int episodeId, long userId, int durationMin) {
        boolean alreadyInIt = episodeDAO.isAdded(userId, showId, seasonId, episodeId, durationMin);
        if (!alreadyInIt) {
            Episode newEpisode = new Episode();
            newEpisode.setDuration_min(durationMin);
            newEpisode.setShow_id(showId);
            newEpisode.setSeason_id(seasonId);
            newEpisode.setEpisode_id(episodeId);
            newEpisode.setUser_id(userId);
            episodeDAO.save(newEpisode);
        }
        return alreadyInIt;
    }

    @PostMapping("/timeSpent")
    public int timeSpent(long userId) {
        return episodeDAO.sumTimeSpent(userId);
    }

    @PostMapping("/removeEpisode")
    public boolean removeEpisode(long showId, int seasonId, int episodeId, long userId, int durationMin) {
        boolean exist = episodeDAO.isAdded(userId, showId, seasonId, episodeId, durationMin);
        if (exist) {
            Episode episodeToRemove = episodeDAO.episodeToDelete(userId, showId, seasonId, episodeId, durationMin);
            episodeDAO.delete(episodeToRemove);
        }
        return exist;
    }
}
