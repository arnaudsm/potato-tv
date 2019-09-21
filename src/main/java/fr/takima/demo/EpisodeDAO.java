package fr.takima.demo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface EpisodeDAO extends CrudRepository<Episode, Long> {


    @Query("SELECT SUM (duration_min) FROM seen_episodes WHERE user_id = :id")
    int sumTimeSpent(@Param("id") int id);

    /*@Query("SELECT id FROM seen_episodes WHERE user_id = :id AND show_id = :shId AND season_id = :seId AND episode_id = :epId")
    int numberEpisode (@Param("id") int id, @Param("shId") long showId, @Param("seId") int seasonId, @Param("epId") int episodeId);*/

    @Query("SELECT CASE WHEN COUNT(show_id)>0 THEN TRUE ELSE FALSE END FROM seen_episodes WHERE user_id = :id AND show_id = :shId AND season_id = :seId AND episode_id = :epId")
    boolean addedEpisode (@Param("id") int id, @Param("shId") long showId, @Param("seId") int seasonId, @Param("epId") int episodeId);


    /*@Query("SELECT * FROM seen_episodes WHERE user_id = :id")
    List<String> numberEpisode (@Param("id") int id);*/

}
