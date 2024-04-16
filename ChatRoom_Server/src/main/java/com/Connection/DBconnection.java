package com.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBconnection {
    public EntityManagerFactory emfactory;
    public EntityManager entityManager;

    private static DBconnection conn = null;

    private DBconnection(){
        emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
        entityManager = emfactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    public static synchronized DBconnection getInstance(){
        if(conn == null){
            return new DBconnection();
        }else return conn;
    }

    public void EndTransaction(Object object){
        entityManager.persist(object);
        entityManager.getTransaction().commit();
        entityManager.close();
        emfactory.close();
    }
    public void JustEndTransaction(){

        entityManager.getTransaction().commit();
        entityManager.close();
        emfactory.close();
    }

//    public Object find(Object Class,Object key){
//        return entityManager.getTransaction().find(Class.getClass(),key);
//    }
}
