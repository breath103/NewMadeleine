package com.Madeleine.Biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Madeleine.DAO.MadeleineDAO;
import com.Madeleine.Entity.Madeleine;
import com.Madeleine.Entity.MadeleinePhoto;


@Service
public class MadeleineBizImpl implements MadeleineBiz {
	@Autowired
	private MadeleineDAO madeleineDAO;
	
	@Override
	public List<Madeleine> findAll() 
	{
		return madeleineDAO.findAll();
	}

	@Override
	public Madeleine insert(Madeleine madeleine)
	{
		return madeleineDAO.insert(madeleine);
	}

	@Override
	public Madeleine findByIdx(Integer idx)
	{
		return madeleineDAO.findByIdx(idx);
	}

	@Override
	public void update(Madeleine madeleine)
	{
		madeleineDAO.update(madeleine);
	}

	@Override
	public void delete(Madeleine madeleine)
	{
		madeleineDAO.delete(madeleine);
	}

	@Override
	public MadeleinePhoto insert(MadeleinePhoto photo) {
		return madeleineDAO.insert(photo);
	}
}
