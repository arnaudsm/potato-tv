package fr.takima.demo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 *
 */
@Repository
public interface UserDAO extends CrudRepository<User, Long> {

    @Query("SELECT id FROM users WHERE user_name = :name")
    int userId(@Param("name") String userName);

    /*@Query("SELECT password_hash FROM users WHERE id = :uId")
    Object userPassword(@Param("uId") long id);*/
}
