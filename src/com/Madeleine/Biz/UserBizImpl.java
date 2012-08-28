package com.Madeleine.Biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Madeleine.DAO.UserDAO;
import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

@Service
public class UserBizImpl implements UserBiz {

	
	@Autowired
	private UserDAO userDAO;
	
	
	@Override
	public CONSTRAINTS_CHECK_RESULT checkConstraints(User user){
		if(this.findByEmail(user.getEmail())!= null)
			return CONSTRAINTS_CHECK_RESULT.EMAIL_DUPLICATED;
		else if(this.findByTypeAndID(user.getUserType(), user.getForeginUserId())!=null)
			return CONSTRAINTS_CHECK_RESULT.TYPE_AND_ID_DUPLICATED;
		else 
			return CONSTRAINTS_CHECK_RESULT.SUCCESS;
	}
	@Override
	public User generateUserWithFacebookInfo(String facebook_id,String access_token){
		FacebookClient facebookClient = new DefaultFacebookClient(access_token);		
		com.restfb.types.User fbUser = facebookClient.fetchObject("me",com.restfb.types.User.class);
		return new User(USER_TYPE.FACEBOOK_USER,
									   fbUser.getName(),
									   fbUser.getEmail(),
									   null,
									   facebook_id,
									   access_token);
	}
	@Override
	public User insert(User user){
		return this.userDAO.insert(user);
	}
	@Override 
	public User findByTypeAndID(USER_TYPE user_type,String foregin_id)
	{
		return userDAO.findByTypeAndID(user_type,foregin_id);
	}
	@Override
	public User findByEmail(String email){
		return userDAO.findByEmail(email);
	}
}

