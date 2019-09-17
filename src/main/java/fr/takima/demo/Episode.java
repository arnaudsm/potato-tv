package fr.takima.demo;

import javax.persistence.*;


/**
 *
 */
@Entity(name = "seen_episodes")

public class Episode {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id long id;
    @Column(name = "show_id") long show_id;
    @Column(name = "season_id") int season_id;
    @Column(name = "episode_id") int episode_id;
    @Column(name = "duration_min") int duration_min;
    @Column(name = "user_id") long user_id;

    public Episode(long id, long show_id, int season_id, int episode_id, int duration_min, long user_id) {
        this.id = id;
        this.show_id = show_id;
        this.season_id = season_id;
        this.episode_id = episode_id;
        this.duration_min = duration_min;
        this.user_id = user_id;
    }

    public Episode() {
    }
}
