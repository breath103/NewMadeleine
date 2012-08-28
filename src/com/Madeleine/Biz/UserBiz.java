package com.Madeleine.Biz;

import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;

public interface UserBiz {
	public enum CONSTRAINTS_CHECK_RESULT{
		SUCCESS,
		EMAIL_DUPLICATED,
		TYPE_AND_ID_DUPLICATED
	};
	
	
	User generateUserWithFacebookInfo(String facebook_id, String access_token);
	User findByTypeAndID(USER_TYPE user_type,String foregin_id);
	User findByEmail(String email);
	CONSTRAINTS_CHECK_RESULT checkConstraints(User user);
	User insert(User user);
}
