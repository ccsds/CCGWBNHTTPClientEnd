package com.ccgwbn.sds.start;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.ParseException;
import com.ccgwbn.sds.util.GetDrPeng;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 把一堆已接工单批量自动预约到第二天
 * @author Administrator
 *
 */
public class yuyue {
	public static void main(String[] args) 
 throws IOException, ParseException, URISyntaxException, java.text.ParseException{
		//读取json
		String userId="jlcc_sunhe";
		String userPwd="123456";
		String sessionID="";
		GetDrPeng getDrPeng=new GetDrPeng();
		//登陆，获得sessionId
		sessionID=	getDrPeng.getSessionId(userId, userPwd);
		JSONArray jsonArray=JSONArray.fromObject(getDrPeng.getYijie(sessionID));
		int jsonSize=jsonArray.size()-1;
	
		for(int i=0;i<jsonSize;i++){
	    	JSONObject jsonObject=jsonArray.getJSONArray(i).getJSONObject(0);
			getDrPeng.appointDate(sessionID, jsonObject.getString("processInstId"));
			
		}
	}
}
