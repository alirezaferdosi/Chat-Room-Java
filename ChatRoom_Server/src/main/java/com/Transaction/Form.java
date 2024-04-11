package com.Transaction;

import com.Model.*;

import javax.persistence.GenerationType;
import javax.persistence.Query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static com.Connection.DBconnection.getInstance;

public class Form {
    private User user;

    public Form(){
        user = new User();
    }

    public Boolean Register(String username, String password){
        user.setUsername(username);
        user.setPassword(password);
        getInstance().EndTransaction(user);
        return true;
    }

    public Boolean Login(String username, String password){
        user = getInstance().entityManager.find(User.class,username);
        return user.getPassword().equals(password);
    }

    public List GetGroup(String username){
        User user = getInstance().entityManager.find(User.class, username);
        Query query = getInstance().entityManager.createNamedQuery("select groups");
        query.setParameter("user", user);

        return query.getResultList();

    }

    public void CreateGroup(String name, User owner, String information){
        Group group = new Group();
        group.setName(name);
        group.setOwner(owner);
        group.setInformation(information);

        getInstance().EndTransaction(group);
    }

    public Boolean AddUser(User user,Group group){

        Query query = getInstance().entityManager.createNamedQuery("Checking whether a user is a member");
        query.setParameter("u",user);
        query.setParameter("g",group);

        if(!Membership(user,group)){
            Member member = new Member();
            member.setUser(user);
            member.setGroup(group);
            getInstance().EndTransaction(member);
            return true;
        }else return false;

    }

    public Boolean RemoveUser(User user, Group group){
        if(Membership(user,group)){
            Query query = getInstance().entityManager.createQuery("DELETE FROM Member e WHERE e.user = :u AND e.group = :g");
            query.setParameter("u",user);
            query.setParameter("g",group);
            query.executeUpdate();
            return true;
        }else return false;

    }

    private Boolean Membership(User user, Group group){
        Query query = getInstance().entityManager.createNamedQuery("Checking whether a user is a member");
        query.setParameter("u",user);
        query.setParameter("g",group);

        return !query.getResultList().isEmpty();


    }

}
