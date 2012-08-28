package com.Madeleine.DAO;

import java.util.List;
import com.Madeleine.Entity.*;
import com.Madeleine.Entity.Madeleine.MadeleineSendState;

public interface MadeleineDAO {
	public List<Madeleine> findAll();

	Madeleine insert(Madeleine madeleine);
	Madeleine findByIdx(Integer idx);
	
	MadeleinePhoto insert(MadeleinePhoto photo);
	
	void update(Madeleine madeleine);
	void delete(Madeleine madeleine);
}
