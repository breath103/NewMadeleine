package com.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;


import org.springframework.transaction.annotation.*;

import com.Facebook.FacebookInfoBiz;
import com.Madeleine.Biz.MadeleineBiz;
import com.Madeleine.Biz.UserBiz;
import com.Madeleine.Biz.UserBiz.CONSTRAINTS_CHECK_RESULT;
import com.Madeleine.Entity.Madeleine;
import com.Madeleine.Entity.Madeleine.MadeleineSendState;
import com.Madeleine.Entity.MadeleinePhoto;
import com.Madeleine.Entity.MadeleinePhoto.SOURCE_TYPE;
import com.Madeleine.Entity.User;
import com.Madeleine.Entity.User.USER_TYPE;
import com.Madeleine.service.UserService;
import com.common.HibernateUtil;
import com.view.SlideShow;
import com.JSON.ExcludeValue;

@Controller
@Transactional
public class MadeleineController {
	
	private final MadeleineBiz madeleineBiz;
	private final UserBiz userBiz;
	private final FacebookInfoBiz facebookInfoBiz;
	
	private SessionFactory sessionFactory;
	
	
	@Autowired  
	public MadeleineController(MadeleineBiz madeleineBiz,
							   UserBiz userBiz,
							   FacebookInfoBiz facebookInfoBiz,
							   SessionFactory sessionFactory) {
		this.madeleineBiz	 = madeleineBiz;
		this.userBiz 		 = userBiz;
		this.facebookInfoBiz = facebookInfoBiz;
		this.sessionFactory	 = sessionFactory;
	}
	
	@RequestMapping(value = "/joinWithFacebookInfo.m",method = RequestMethod.POST)
	public ModelAndView joinWithFacebookInfo(String facebook_id,String access_token,
			String name,String email,HttpSession httpSession){
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/jsonView");
	
		
		JSONObject resJson = new JSONObject();
		resJson.put("result", "fail");
		
		if(facebook_id == null || access_token == null || name == null || email == null){
			resJson.put("reason", "param is null");
		}
		else {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
		
			User user = UserService.userWithTypeAndForeignID(session, USER_TYPE.FACEBOOK_USER, facebook_id);
			///중복 메일을 사용하는사람이 있는지는 일단 체크하지 않는다.
			if(user != null){
				JsonConfig config = new JsonConfig();
				config.setExcludes(new String[]{"madeleineList"});
				resJson.put("user"	, JSONObject.fromObject(user,config));
				resJson.put("reason", "이미 등록된 페이스북 계정입니다");
			}
			else{
				user = User.generateWithFacebookInfo(facebook_id, access_token, name, email);
				System.out.println("+++" + session.save(user));
				
				if(user != null){
					JsonConfig config = new JsonConfig();
					config.setExcludes(new String[]{"madeleineList"});
					resJson.put("result", "success");
				}
				else
				{
					resJson.put("reason", "DB입력에 실패하였습니다");
				}
			}
			user.hibernateInitialize();
			httpSession.setAttribute(User.sessionAttributeName, user);
			
			session.getTransaction().commit();   
			session.close();
		}
		mav.addObject("data", resJson.toString());
		
		return mav;
		
		
		/*
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		JSONObject resJson = new JSONObject();
		if(facebook_id == null || access_token == null){
			resJson.put("result", "fail");
			resJson.put("reason", "param is null");
		}else {
			User user = userBiz.generateUserWithFacebookInfo(facebook_id, access_token);
			switch(userBiz.checkConstraints(user))
			{
				case SUCCESS:{
					user = userBiz.insert(user);
					if(user != null){
						JsonConfig config = new JsonConfig();
						config.setExcludes(new String[]{"madeleines"});
						resJson.put("result", "success");
						resJson.put("user", JSONObject.fromObject(user,config));
					}
					else
					{
						resJson.put("result", "fail");
						resJson.put("reason", "DB입력에 실패하였습니다");
					}
				}break;
				/*
				case EMAIL_DUPLICATED : {
					resJson.put("result", "fail");
					resJson.put("reason", "이미 사용중인 이메일 입니다");
				}break;
				*/
		/*
				case TYPE_AND_ID_DUPLICATED : {
					resJson.put("result", "fail");
					resJson.put("reason", "이미 사용중인 페이스북 계정입니다");
				}break;
			}
		}
		mav.addObject("data", resJson.toString());
		
		return mav;
		*/
	}
	
	@RequestMapping("/getUserWithFacebookInfo.m")
	public ModelAndView getUserWithFacebookInfoHandler(String facebook_id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		JSONObject resJson = new JSONObject();
		if(facebook_id == null){
			resJson.put("result", "fail");
			resJson.put("reason", "param is null");
		}else{
			User user = userBiz.findByTypeAndID(USER_TYPE.FACEBOOK_USER, facebook_id);
			if(user==null){
				resJson.put("result", "fail");
				resJson.put("reason", "cannot find user");
			}
			else{
				JsonConfig config = new JsonConfig();
				config.setExcludes(new String[]{"user"});
				resJson.put("result", "success");
				resJson.put("user", JSONObject.fromObject(user,config));
			}
		}
		mav.addObject("data", resJson.toString());
		return mav;
	}
	@RequestMapping("/addPhotoToMadeleine.m")
	public ModelAndView addPhotoToMadeleineHandler(String src){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Madeleine madeleine = (Madeleine)session.get(Madeleine.class, Integer.valueOf("370"));
		MadeleinePhoto photo = new MadeleinePhoto();
		photo.setSrc(src);
		photo.setMadeleine(madeleine);
		madeleine.getPhotos().add(photo);
		
		session.save( photo );
		session.getTransaction().commit();
		
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"madeleine","user"});
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		mav.addObject("data",JSONObject.fromObject(madeleine,config).toString());
		
		session.close();
		return mav;
	}
	

	@RequestMapping("/getMadeleineList.m")
	public ModelAndView getMadeleineListHandler(String state){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		User user = (User)session.get(User.class, 21);
		List<Madeleine> madeleineList = user.getMadeleineInState(session, MadeleineSendState.SENDED);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"photos","user"});
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		mav.addObject("data",JSONArray.fromObject(madeleineList,config).toString());
		
		session.close();
		return mav;
	}
	
	
	@RequestMapping("/slideShow.m")
	public ModelAndView slideShowHandler(String id){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("slideShow/slideShow");
		
		if(id!=null){
			Madeleine madeleine = (Madeleine)session.get(Madeleine.class, Integer.valueOf(id));
			madeleine.hibernateInitialize();  //force loading lazy loaded photos;
			switch(madeleine.getSendState()){
				case NOT_SENDED : {
				}break;
				case SENDED :{
					madeleine.setSendState(MadeleineSendState.OPENED);
				}
				case OPENED : {

					mav.addObject("madeleine",madeleine);
					mav.addObject("jsonMadeleine", SlideShow.toJSONString(madeleine));
				}break; 
			}
		}
		
		session.getTransaction().commit();
		session.close();
		return mav;
	}
	@RequestMapping("/user/userInfo")
	public ModelAndView userInfoHandler(HttpSession httpSession){
		ModelAndView mav = new ModelAndView();
		User user = (User)httpSession.getAttribute(User.sessionAttributeName);
		if(user != null){
			System.out.println(user.getName());
			mav.addObject("user",user);
			mav.setViewName("user/userInfo");
		}
		else 
		{
			mav.setViewName("index.jsp");
		}
		return mav;
	}
	
	@RequestMapping("/user/editUserInfo")
	public String editUserInfoHandler(HttpServletRequest request,
									  HttpSession httpSession){
		User user = (User)httpSession.getAttribute(User.sessionAttributeName);
		if(user != null){
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			
			user.setName(request.getParameter("user.name"));
			user.setEmail(request.getParameter("user.email"));
			session.update(user);
			
			session.getTransaction().commit();
			session.close();
			user.hibernateInitialize();
		}
		else 
		{
			return null;
		}
		return "redirect:/user/userInfo.m";
	}
	
	
	@RequestMapping("/madeleine/create.m")
	public ModelAndView madeleineCreateHandler(String name,String photos,HttpSession httpSession){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		
		Madeleine madeleine = new Madeleine();
		madeleine.setUser((User)httpSession.getAttribute(User.sessionAttributeName));
		madeleine.setIsPublic(false);
		madeleine.setName(name);
		madeleine.setSendState(MadeleineSendState.SENDED);
		Timestamp currentTimestamp = new Timestamp( (new Date()).getTime());
		madeleine.setReservedTime(currentTimestamp);
		madeleine.setCreatedTime(currentTimestamp);
		session.save(madeleine);
		
		//PHOTO 
		/*
		 	src
		 	description
		 	sourceType 
		 	sourceID
		 */
		JSONArray photoArray = JSONArray.fromObject(photos);
		for(int i=0;i<photoArray.size();i++)
		{
			JSONObject jsonPhoto = photoArray.getJSONObject(i);
			System.out.println(jsonPhoto.toString());
			MadeleinePhoto photo = new MadeleinePhoto();
			photo.setMadeleine(madeleine);
			photo.setSrc(jsonPhoto.getString("src"));
			if(jsonPhoto.containsKey("description"))
				photo.setDescription(jsonPhoto.getString("description"));
			photo.setSourceType(SOURCE_TYPE.valueOf(jsonPhoto.getString("sourceType")));
			photo.setSourceID(jsonPhoto.getString("sourceID"));
			session.save(photo);
		}
		session.getTransaction().commit();
		session.close();
		
		JSONObject result = new JSONObject().element("result", "success");
	
		mav.addObject("data",result.toString());
		return mav;
	}
}
