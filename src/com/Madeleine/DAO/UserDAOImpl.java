package com.Madeleine.DAO;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.Madeleine.Entity.Madeleine;
import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;

import java.util.List;

@Repository("userDAO")
public class UserDAOImpl extends HibernateDaoSupport implements UserDAO{
	@Autowired
	public UserDAOImpl(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	@Override
	public User insert(User user){
		Integer result = (Integer)this.getHibernateTemplate().save(user);
		if(result > 0)
			return user;
		else 
			return null;
	}
	@Override
	public void update(User user){
		this.getHibernateTemplate().update(user);
	}
	@Override
	public void delete(User user){
		getHibernateTemplate().delete(user);
	}
	@Override
	public User findByTypeAndID(USER_TYPE user_type,String foregin_id)
	{
		List<User> result = this.getHibernateTemplate().find("from User where userType = ? and foreginUserId = ?",new Object[]{user_type,foregin_id});
		if(result.size() > 0) return result.get(0);
		else return null;
	}
	
	@Override
	public User findByEmail(String email) 
	{
		List<User> result = this.getHibernateTemplate().find("from User where email = ?",email);
		if(result.size() > 0) return result.get(0);
		else return null;
	}	
}
