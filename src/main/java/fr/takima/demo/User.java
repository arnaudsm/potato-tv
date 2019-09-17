package fr.takima.demo;

import javax.persistence.*;

/**
 *
 */
@Entity(name = "users")

public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id long id;
    @Column(name = "user_name") String userName;
    @Column(name = "password_hash") String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

