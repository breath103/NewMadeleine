package com.Facebook;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.Madeleine.Entity.Madeleine;


@Repository("facebookInfoDAO")
public class FacebookInfoDAOImpl extends HibernateDaoSupport implements FacebookInfoDAO{
	
	@Autowired
	public FacebookInfoDAOImpl(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FacebookInfo findByFacebookId(String id){
		return (FacebookInfo)((List)getHibernateTemplate().find("from FacebookInfo where facebook_id = ?",id)).get(0);
	}
}	
