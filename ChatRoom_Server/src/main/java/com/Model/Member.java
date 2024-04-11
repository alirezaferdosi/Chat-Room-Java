package com.Model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name ="_Member_")
@NamedQuery(name = "Checking whether a user is a member", query = "SELECT m.Id FROM Member m WHERE m.user = :u AND m.group = :g")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;


    @Id
    @JoinColumn(name = "users", nullable = false)
    private User user;

    @Id
    @JoinColumn(name = "groups", nullable = false)
    private Group group;

    public Long getId(){
        return Id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
