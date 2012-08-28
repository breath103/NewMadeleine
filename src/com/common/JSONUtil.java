package com.common;

import java.sql.Timestamp;

import com.Madeleine.Entity.Madeleine;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JSONUtil {
	public static final JsonValueProcessor toStringValueProcessor = (new JsonValueProcessor(){
		public Object processArrayValue(Object arg0, JsonConfig arg1) { return null; }
		public Object processObjectValue(String key, Object value,JsonConfig conf) { return value.toString(); }
	});
	
}
