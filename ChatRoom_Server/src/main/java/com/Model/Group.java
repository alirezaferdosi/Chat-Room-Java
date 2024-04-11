package com.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "_Group_")
@NamedQuery(name = "select groups", query = "SELECT g.Id from Group g WHERE g.owner = :user")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @Column(name = "Name", nullable = false)
    private String Name;

    @Column(name = "Information")
    private String Information;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "Owner", nullable = false)
    private User owner;


    public Group(String name, String information) {
        super();
        Name = name;
        Information = information;
    }

    public Group() {
        super();
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
