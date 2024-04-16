package com.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "_User_")
public class User implements Serializable{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @Id
    @Column(name = "Username", nullable = false)
    private String Username;


    @Column(name = "Password", nullable = false)
    private String Password;


    public Long getId() {
        return Id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



}
