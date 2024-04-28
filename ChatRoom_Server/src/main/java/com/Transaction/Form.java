package com.Transaction;

import com.Model.*;
import org.json.simple.JSONObject;

import javax.persistence.Query;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.Connection.DBconnection.getInstance;
import static com.ServerController.Server.OnlineOnGroup;

public class Form implements Serializable {
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

    public JSONObject GetGroup(String username) {
        JSONObject json = new JSONObject();
        Group group;
        User user = getInstance().entityManager.find(User.class, username);

        Query query = getInstance().entityManager.createNamedQuery("select all group of member");
        query.setParameter("user", user);
        List list = query.getResultList();

        for (Object o: list) {
            group = (Group) o;
            json.put(group.getId(),group.getName());
        }
        return json;
    }

    public Long CreateGroup(String name, String owner, String information){
        Group group = new Group();
        Member member = new Member();
        User user = getInstance().entityManager.find(User.class, owner);
        group.setName(name);
        group.setOwner(user);
        group.setInformation(information);


        member.setGroup(group);
        getInstance().EndTransaction(group);

        member.setUser(user);
        getInstance().EndTransaction(member);

        Long id =  group.getId();
        OnlineOnGroup.put(id,null);
        try {
            Files.createFile(Paths.get("src/Group/" + id + ".txt"));
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long CreateGroup(String name, String owner){
        Group group = new Group();
        Member member = new Member();
        User user = getInstance().entityManager.find(User.class, owner);


        group.setName(name);
        group.setOwner(user);
        getInstance().EndTransaction(group);

        member.setGroup(group);
        member.setUser(user);
        getInstance().EndTransaction(member);

        Long id =  group.getId();
        OnlineOnGroup.put(id,null);

        try {
            Files.createFile(Paths.get("src/Group/" + id + ".txt"));
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String AddMemeber(String user, Long group, String admin){
        User A = getInstance().entityManager.find(User.class,admin);
        User u = getInstance().entityManager.find(User.class,user);
        Group g = getInstance().entityManager.find(Group.class, group);

        if(Isadmin(A,g)) return "you are not admin";
        if (!Membership(user, group)) {
            Member member = new Member();
            member.setUser(u);
            member.setGroup(g);
            getInstance().EndTransaction(member);
            return user + " became a member";
        } else return user + " Currently a member";

    }

    public String RemoveUser (String user, Long group, String admin){
        User A = getInstance().entityManager.find(User.class,admin);
        User u = getInstance().entityManager.find(User.class,user);
        Group g = getInstance().entityManager.find(Group.class,group);

        if(Isadmin(A,g)) return "you are not admin";
        if (Membership(user,group)) {
                Query query1 = getInstance().entityManager.createNamedQuery("Checking whether a user is a member");
                query1.setParameter("u", u);
                query1.setParameter("g", g);
                List list = query1.getResultList();
                for (Object id: list){
                    Member member =getInstance().entityManager.find(Member.class,id);
                    getInstance().entityManager.remove(member);
                    getInstance().JustEndTransaction();
                    return user + " removed";
                }
                return "you can't remove this user";
            } else return user + " is not in the group";
        }

    public Boolean Membership (String username, Long groupid){
            User user = getInstance().entityManager.find(User.class,username);
            Group group = getInstance().entityManager.find(Group.class,groupid);
            Query query = getInstance().entityManager.createNamedQuery("Checking whether a user is a member");
            query.setParameter("u", user);
            query.setParameter("g", group);

            return !query.getResultList().isEmpty();


    }

    private Boolean Isadmin(User user, Group group){
        Query query = getInstance().entityManager.createNamedQuery("chack the group admin");
        query.setParameter("O",user);
        query.setParameter("id",group.getId());
        return query.getResultList().isEmpty();
    }

    public static List GetAllgroups(){
            return getInstance().entityManager.createNamedQuery("all groups").getResultList();
    }

    public void OnlineOnGroup(String username, Long group){
        ArrayList<String> list = OnlineOnGroup.get(group);
        list.add(username);
        OnlineOnGroup.put(group, list);
    }

}
