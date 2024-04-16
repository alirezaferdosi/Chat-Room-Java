package com.Datareview;

import com.Model.Group;
import com.Model.User;

import static com.Connection.DBconnection.getInstance;

public class DataCheck {

    public Boolean UserExist(String username){
        User user = getInstance().entityManager.find(User.class, username);
        return user != null;
    }
    public Boolean GroupExist(Long id){
        Group group = getInstance().entityManager.find(Group.class,id);
        return  group != null;
    }

}
