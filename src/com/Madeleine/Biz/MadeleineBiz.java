package com.Madeleine.Biz;


import java.util.List;
import com.Madeleine.Entity.Madeleine;
import com.Madeleine.Entity.MadeleinePhoto;

public interface MadeleineBiz {
	public List<Madeleine> findAll();
	public Madeleine insert(Madeleine madeleine);
	public MadeleinePhoto insert(MadeleinePhoto photo);
	public Madeleine findByIdx(Integer idx);
	public void update(Madeleine madeleine);
	public void delete(Madeleine madeleine);
}
