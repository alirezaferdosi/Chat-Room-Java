package com.Datareview;

import com.Model.User;

import static com.Connection.DBconnection.getInstance;

public class DataCheck {

    public Boolean UserExist(String Username){
        User user = getInstance().entityManager.find(User.class, Username);
        return user != null;
    }

}
