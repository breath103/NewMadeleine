package com.Madeleine.service;

import org.hibernate.Session;

import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;

public class UserService {
	public static User userWithID(Session session,Integer id){
		return (User)session.get(User.class, id);
	}
	public static User userWithTypeAndForeignID(Session session,USER_TYPE type,String foreginUserId){
		return (User)session.createQuery("from User where userType = ? and foreginUserId = ?")
								.setParameter(0, type)
								.setParameter(1, foreginUserId).uniqueResult();
	}
	public static User userWithEmail(Session session,String email){
		return (User)session.createQuery("from User where email = ?")
								.setParameter(0, email).uniqueResult();
	}
	
	
}
