package com.ccgwbn.sds.util;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.ccgwbn.sds.pojo.UserLink;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetDrPeng {
	
	private static Logger logger = Logger.getLogger("console");

	/***
	 * 预约工单需要一个工单编号，懒得自己生成，这样弄一下试试能不能获取到
	 * @param processInstId
	 * @param taskInstId
	 * @return
	 */
	public String getOrderno(String sid,String processInstId,String taskInstId){
		//logger.info("taskInstId:"+taskInstId);
		
		String orderno="";
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/w?";
		url=url+"sid="+sid
			+"&cmd=CLIENT_BPM_FORM_MAIN_PAGE_OPEN"
			+"&processInstId="+processInstId
			+"&taskInstId="+taskInstId
			+"&openState=1";
		
		String repsonse=getUrlResponse(url);
	
		Document document=Jsoup.parse(repsonse);
		
		orderno=JSONObject.fromObject(document.getElementById("oldFormData").html()).getString("ORDERNO");
		//logger.info("工单编号"+orderno);
		return orderno;
	}
	/**获取工单集合
	 * 拆开工单集合
	 * 预约工单
	 * 预约工单这里有个包没抓到，现在就是点完预约，时间能设置成功，但是没有操作记录
	 * 已能成功预约
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws java.text.ParseException 
	 */
	public String appointDate(String sid,String processInstId) throws URISyntaxException, ParseException, IOException, java.text.ParseException{

		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.workbench_task_datalist&sid="+sid+"&boxName=todo&groupName=noGroup&start=1&owner=&title=&begin=&end=&iobd=&ior&ios=&ioc=";
		String str = null;
		
		str=getUrlResponse(url);
		//System.out.println("已接工单信息"+str);
		
		JSONArray jsonArray=JSONArray.fromObject(str);
		//System.out.println(jsonArray);
	     String time="";
	      //预约工单
	     url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.wos_appoint&";
	     int aSize=jsonArray.size()-1;
	     JSONObject jsonObject=null;
	    //遍历已接工单
	     for(int i=0;i<aSize;i++){

	    	 jsonObject=jsonArray.getJSONArray(i).getJSONObject(0);

	        if(processInstId.equals(jsonObject.get("processInstId"))){  
	        	jsonObject=jsonArray.getJSONArray(i).getJSONObject(0);
	        
	        	 Date date = new Date();// 新建此时的的系统时间
	             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	        	url=url+"sid="+sid
	       	    +"&taskInstId="+jsonObject.get("id")
	       	    +"&processInstId="+jsonObject.get("processInstId")
	       	    +"&orderno="+getOrderno(sid,jsonObject.get("processInstId")+"",jsonObject.get("id")+"")
	       	    +"&processDefId=obj_e453ae6613a245b39abbefb8f99f9094"
	       	    +"&appointdate="+   df.format(getNextDay(date))+"+"+"20%3A01" ; 	 
	         	}
	    	  /* if(processInstId.equals(jsonObject.get("processInstId"))){  
		        	
		        	jsonObject=jsonArray.getJSONArray(i).getJSONObject(0);
		       	   time=(String)jsonObject.get("beginTime");
		       	   url=url+"sid="+sid
		       	   +"&taskInstId="+jsonObject.get("id")
		       	   +"&processInstId="+jsonObject.get("processInstId")
		       	   +"&orderno="+getOrderno(sid,jsonObject.get("processInstId")+"",jsonObject.get("id")+"")
		       	   +"&processDefId=obj_e453ae6613a245b39abbefb8f99f9094"
		       	   +"&appointdate="+time.split(" ")[0]+"+"+"20%3A01" ; 	 
		         	}*/
	      }url+="&type=pc";
	
		return getUrlResponse(url);
	}
	/**
	 * 返回下一天
	 * @param date
	 * @return
	 */
	   public static Date getNextDay(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
	        date = calendar.getTime();
	        return date;
	    }
	/**
	 * 接单 
	 * @return
	 */
	public  String chackTask(String sid,UserLink userLink){
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.workbench_con_taskid&sid="+sid+"&taskid="+userLink.getTaskid();
		String chackLog=getUrlResponse(url);
		return chackLog;
	}
//	String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.workbench_con_taskid&sid="+sessionId+"&taskid="+taskId;

	/**
	 * 遍历jsonArr判断社区信息
	 * @return Arraylist<UserLink>
	 * 符合条件的封装成UserLink 添加到Arraylist集合中，传到接单处遍历接单
	 * 但是网络情况会不会导致有些bug
	 */
	public ArrayList<UserLink> selectArray(JSONArray jsonArr) {
	
		//临时变量用
		JSONObject ask=null;
		 ArrayList<UserLink> userList = new ArrayList<UserLink>();
		 UserLink userLink=new UserLink();
		//遍历当前jsonArray
		for(int i=0;i<jsonArr.size();i++){
			ask=jsonArr.getJSONObject(i);
			userLink.setOrderno(ask.getString("ORDERNO"));
			userLink.setCommun(ask.getString("COMMUNITYNAME"));
			userLink.setLink(ask.getString("LINKADDRESS"));
			userLink.setTaskid(ask.getString("INSTANCEID"));
			userLink.setTal(ask.getString("LINKTEL"));
			userLink.setLinkMan(ask.getString("LINKMAN"));
			userLink.setCustomerno(ask.getString("CUSTOMERNO"));
			logger.info(userLink.toString());
			userList.add(userLink);

		}
		return userList;
	}
	/**通过sessionId 获取工单列表的json集
	 * 获取工单信息
	 * @return jsonArray
	 */
	public JSONArray getTask(String sid){
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.workbench_con_taskdata&sid="+sid;
		JSONArray  jsonArr= JSONArray.fromObject(getUrlResponse(url));
		return jsonArr;
	}
	/**
	 * 获取当前工单数
	 * @param userName
	 * @param pwd
	 * 返回Integer
	 */
	public  Integer getCount(String sid,String userName,String pwd){
		// sid=getSessionId(userName,pwd);
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?cmd=com.actionsoft.apps.workbench_con_task&sid="+sid+"&runType=1";
		JSONObject json=(JSONObject)JSONObject.fromObject(getUrlResponse(url));
		int count=0;
		try {
			count=Integer.parseInt(json.getJSONObject("data").getString("count"));

		} catch (Exception e) {
			System.out.print("获取工单数失败");
		}
		logger.info("当前工单数:"+count);
		return count;
	}
	/**
	 *  传入 userName 和 用户密码 获取到sessionId
	 * @param userName
	 * @param pwd
	 * @return sid
	 */
	public String getSessionId(String userName,String pwd) {
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd?userid="+userName+"&pwd="+pwd+"&rememberMeUid=on&rememberMePwd=on&lang=cn&cmd=CLIENT_USER_LOGIN&sid=&deviceType=pc";
		JSONObject json=(JSONObject)JSONObject.fromObject(getUrlResponse(url));//
		logger.info("登陆成功，当前用户："+userName);
		return json.getJSONObject("data").getString("sid");
	}
	/***
	 * 已接工单列表
	 */
	public String  getYijie(String sid){
		String url="http://dzgd.drpeng.com.cn:8079/portal/r/jd";
		url=url+"?cmd=com.actionsoft.apps.workbench_task_datalist&sid="+sid+"&boxName=todo&groupName=noGroup&start=1&owner=&title=&begin=&end=&iobd=&ior=&ios=&ioc=";
		return getUrlResponse(url);
	}
	/**
	 * 传入 url 
	 * @param url
	 * @return json字符串
	 */
	public static String getUrlResponse (String url) {
		//System.out.println(url);
		String json = null;
		CloseableHttpClient client=null;
		HttpEntity entity=null;
		try{
		//获取CloseableHttpClient对象
		client=HttpClients.createDefault();
		//创建httpPost对象
		HttpPost post=new HttpPost();
		post.setHeader("Host","dzgd.drpeng.com.cn:8079");
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:65.0) Gecko/20100101 Firefox/65.0");
		post.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		post.setHeader("Accept-Encoding","gzip, deflate");
		post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		post.setHeader("DNT","1");
		post.setHeader("Connection","keep-alive");
		post.setHeader("Upgrade-Insecure-Requests","1");
		post.setHeader("Cache-Control","max-age=0");
		post.setURI(new URI(url));
		//执行client 获取response
		HttpResponse response=client.execute(post);
		entity=response.getEntity();
		json=EntityUtils.toString(entity, "UTF-8");
		//返回json字符串
			return json;
		}catch(HttpHostConnectException hce){
			logger.error("连接超时");		
		}catch (ConnectException ce) {
			logger.error("连接超时...");	
		}catch(ClientProtocolException e){
			e.printStackTrace();
			//return "{\"data\":{\"sid\":\"异常：执行client抛出的异常\"}}";
		}catch(URISyntaxException e){
			e.printStackTrace();
			//return "{\"data\":{\"sid\":\"异常：URI抛出的异常，非要我处理，烦人\"}}";
		}catch(UnknownHostException e){
			e.printStackTrace();
			//return "{\"data\":{\"sid\":\"异常：没什么大毛病，可能是服务器崩了，可能是本地断网了，也可能是我把网址写错了\"}}";
		}catch(IOException e){
			e.printStackTrace();
			//return "{\"data\":{\"sid\":\"异常：居然真发生IO异常了？\"}}";
		}finally{
			 if (entity != null) {
	                try {
	                    EntityUtils.consume(entity);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
			 if (client != null) {
	                try {
	                	//关闭连接
	                    client.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
			
			}//finally的结束大括号
		return "{\"data\":{\"sid\":\"运行异常？下面的操作可能是无效操作，请自己查看小鹏\"}}";
	
	}//end 	 getUrlResponse()	
}
