package com.Facebook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookInfoBizImpl implements FacebookInfoBiz {
	@Autowired
	private FacebookInfoDAO facebookInfoDAO;
	
	@Override
	public FacebookInfo findByFacebookID(String facebook_id)
	{
		return facebookInfoDAO.findByFacebookId(facebook_id);
	}
}
