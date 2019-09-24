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

    public long getId() {
        return id;
    }

    public long getShow_id() {
        return show_id;
    }

    public void setShow_id(long show_id) {
        this.show_id = show_id;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public int getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(int episode_id) {
        this.episode_id = episode_id;
    }

    public int getDuration_min() {
        return duration_min;
    }

    public void setDuration_min(int duration_min) {
        this.duration_min = duration_min;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
