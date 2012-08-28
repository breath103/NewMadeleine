package com.Madeleine.DAO;

import java.util.List;


import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.Madeleine.Entity.*;
import com.Madeleine.Entity.Madeleine.MadeleineSendState;

@Repository("madeleineDAO")
public class MadeleineDAOImpl extends HibernateDaoSupport implements MadeleineDAO {
	
	@Autowired
	public MadeleineDAOImpl(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Madeleine> findAll() {
		return (List<Madeleine>)getHibernateTemplate().loadAll(Madeleine.class);
	}
	
	@Override
	public Madeleine insert(Madeleine madeleine){
		if((Integer)getHibernateTemplate().save(madeleine) > 0)
			return madeleine;
		else 
			return null;
	}
	@Override
	public Madeleine findByIdx(Integer idx){
		List<Madeleine> result = getHibernateTemplate().find("from Madeleine where idx = ?",idx);
		if(result.size() > 0) return result.get(0);
		else return null;
	}
	@Override
	public void update(Madeleine madeleine){
		getHibernateTemplate().update(madeleine);
	}
	@Override
	public void delete(Madeleine madeleine){
		getHibernateTemplate().delete(madeleine);
	}

	@Override
	public MadeleinePhoto insert(MadeleinePhoto photo) {
		if((Integer)getHibernateTemplate().save(photo) > 0)
			return photo;
		else 
			return null;
	}
	
}
