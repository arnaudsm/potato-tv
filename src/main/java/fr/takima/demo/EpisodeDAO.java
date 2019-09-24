package fr.takima.demo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 *
 */
@Repository
public interface EpisodeDAO extends CrudRepository<Episode, Long> {

    @Query("SELECT SUM (duration_min) FROM seen_episodes WHERE user_id = :id")
    public Long sumTimeSpent(@Param("id") long id);

    @Query("SELECT CASE WHEN COUNT(show_id)>0 THEN TRUE ELSE FALSE END FROM seen_episodes WHERE user_id = :id AND show_id = :shId AND season_id = :seId AND episode_id = :epId AND duration_min = :durId")
    boolean isAdded (@Param("id") long id, @Param("shId") long showId, @Param("seId") int seasonId, @Param("epId") int episodeId, @Param("durId") int durationMin);

    @Query("SELECT DISTINCT show_id FROM seen_episodes WHERE user_id = :id")
    List<String> myShow(@Param("id") long id);

    @Query("SELECT show_id, season_id, episode_id, duration_min FROM seen_episodes WHERE user_id = :id")
    List<String> mySeries(@Param("id") long id);

    @Modifying
    @Query("delete from seen_episodes where user_id = :user_id AND show_id = :shId AND season_id = :seId AND episode_id = :epId")
    void deleteEpisode(@Param("user_id") long user_id, @Param("shId") long showId, @Param("seId") int seasonId, @Param("epId") int episodeId);

}
