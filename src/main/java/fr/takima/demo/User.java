package fr.takima.demo;

import javax.persistence.*;

/**
 *
 */
@Entity(name = "users")

public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id long user_id;
    @Column(name = "user_name") String user_name;
    @Column(name = "password_hash") String password;

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User() {
    }
}

