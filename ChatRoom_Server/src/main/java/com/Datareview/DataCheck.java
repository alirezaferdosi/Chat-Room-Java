package com.Datareview;

import com.Model.User;

import static com.Connection.DBconnection.getInstance;

public class DataCheck {

    public Boolean UserExist(String username){
        User user = getInstance().entityManager.find(User.class, username);
        return user != null;
    }

}
