package com.view;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.*;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import com.Madeleine.Entity.Madeleine;
import com.Madeleine.Entity.MadeleinePhoto;
import com.Madeleine.Entity.User;
import com.common.JSONUtil;

public class SlideShow {
	public static String toJSONString(Madeleine madeleine){
		/*
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"user","photos"});
		JSONObject json = JSONObject.fromObject(madeleine,config);
	
		System.out.println("---" + madeleine.getClass().getFields());
		madeleine.getClass().getDeclaredMethods()[0]
		for(Field field : madeleine.getClass().getDeclaredFields()){
			System.out.println("---" + field + " : "+field.getType());
			if(field.getType() == Timestamp.class)
			{
				try {
					System.out.println(field);
					json.element(field.getName(),
							String.format("new Date('%s')",field.get(madeleine).toString()));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		*/
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"user"});
		config.registerJsonValueProcessor(Timestamp.class, JSONUtil.toStringValueProcessor);
		config.registerJsonValueProcessor(MadeleinePhoto.class, (new JsonValueProcessor(){
			public Object processArrayValue(Object value, JsonConfig config) {
				return this.processObjectValue(null, value, config);
			}
			public Object processObjectValue(String key, Object value,JsonConfig config) {
				String excludes[] = config.getExcludes();
				config.setExcludes(new String[]{"madeleine"});
				JSONObject json = JSONObject.fromObject(value,config);
				config.setExcludes(excludes);
				return json;
			}
		}));
		JSONObject json = JSONObject.fromObject(madeleine,config);
	return json.toString();
	}
}
