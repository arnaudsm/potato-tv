package fr.takima.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @GetMapping("/profile")
    public String displayPagePerso() {
        return "profile";
    }


    @GetMapping("/show/{showID}")
    public String displayShow(@PathVariable("showID") long showID, Model model) {
        model.addAttribute("showID", showID);
        return "show";
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
                return new RedirectView("/");
            //else
              //  return new RedirectView("/login");
        } catch (Exception e) {
            System.out.println("ERREUR : " + e);
            return new RedirectView("/login");
        }
    }

    @GetMapping("/exit")
    public RedirectView signOut() {
        return new RedirectView("/");
    }

    @GetMapping(value="/{userId}/library", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showShows(@PathVariable("userId") long id, Model model) {
        List<String> seriesPerso = episodeDAO.myShow(id);
        String viewedEpisodes = String.join(",",seriesPerso);
        return viewedEpisodes;
    }

    @RequestMapping(value = "/timeSpent", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String timeSpent(long userId) {
        Long time = episodeDAO.sumTimeSpent(userId);
        // TODO REQUESTS
        return String.valueOf(time);
    }

    @PostMapping("/addEpisode")
    public void addEpisode(long showId, int seasonId, int episodeId, long userId, int durationMin) {
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
    }

    @GetMapping(value="/isSeen", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String isSeen(long showId, int seasonId, int episodeId, long userId, int durationMin) {
        boolean alreadyInIt = episodeDAO.isAdded(userId, showId, seasonId, episodeId, durationMin);
        if(alreadyInIt) {
            return "1";
        } else {
            return "0";
        }
    }

    @Transactional
    @PostMapping("/removeEpisode")
    public void removeEpisode(long showId, int seasonId, int episodeId, long userId, int durationMin) {
        episodeDAO.deleteEpisode(userId, showId, seasonId, episodeId);
    }
}
