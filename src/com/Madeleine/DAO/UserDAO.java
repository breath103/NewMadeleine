package com.Madeleine.DAO;

import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;

public interface UserDAO {

	User insert(User user);
	void update(User user);
	void delete(User user);
	User findByEmail(String email);
	User findByTypeAndID(USER_TYPE user_type, String foregin_id);

}
