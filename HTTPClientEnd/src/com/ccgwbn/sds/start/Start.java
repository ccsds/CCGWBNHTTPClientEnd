package com.ccgwbn.sds.start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONException;
import com.ccgwbn.sds.pojo.Staff;
import com.ccgwbn.sds.pojo.UserLink;
import com.ccgwbn.sds.util.BASE64;
import com.ccgwbn.sds.util.GetDrPeng;
import com.ccgwbn.sds.util.ReadJSON;
import com.ccgwbn.sds.util.SendMail;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Start {
	static Logger logger = Logger.getLogger("console");
static ReadJSON json=null;
	public static void main(String[] args) {
		//定时器时间为分钟
		int time=6*60;
	System.out.println("版本2.01\n尝试通过回调处理接错单问题     \n定时6分钟  \n工单自动预约测试第7次修改\n有问题请尝试自己解决\n技术支持：天堂小助手\nqq:1414369907");
		json=new ReadJSON();
		try {
			System.out.println("查看配置文件是否乱码："+json.getUserList());
		} catch (IOException e1) {
			System.out.println("配置文件读取失败，请付费联系管理员，或自己解决");
		}
		
	 ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            
            	runn();
             }
        }, 0,time, TimeUnit.SECONDS);//seconds 是秒 
	}
	/**
	 * 当有工单的时候通过传进来的社区进行分拣，返回员工对象
	 * @throws Exception 
	*/
	public static Staff selectJson(String commun) throws Exception{
	
		Staff staff = new Staff();
		JSONArray jsonArray;
		jsonArray = json.getUserList();
		JSONObject jo=null;
		System.out.println("工单分拣中，请稍后。。。。。。。");
		//*默认徐亚全/
		staff.setId("jlcc_xuyaquan");
		staff.setPwd("123456");
		staff.setMail("376456265@qq.com");
		//遍历json文件
		for(int i=1;i<jsonArray.size();i++){
			//遍历jsonArray获得JSONObject
			jo=jsonArray.getJSONObject(i);
			//判断当前循环中的用户社区是否匹配传进来的社区
			/*这里他妈的发现个问题，不会。暂时不知道为什么，在上面给staff个默认值：徐亚全
					
			String a="楼(薄)";
			String c="(薄)";
			System.out.println(a.split(a).length+"  "+a.equals(a));
			System.out.println(c.split(c).length+"  "+c.equals(c));
			结果：1  true
				2  true
			*/
			if(Pattern.matches(jo.getString("sq"), commun)){
	      	     System.out.println("社区匹配成功：");

				//这里base解密一下
				byte[] byteArray = BASE64.decryptBASE64(jo.getString("id"));       
      	       String id=new String(byteArray);
      	     System.out.println("社区匹配成功："+id);
      	     staff=new Staff(id,jo.getString("pas"),jo.getString("sq"),jo.getString("email"));
			}
		}
		return staff;
	}
	/**
	 * 获取工单列表之后将工单列表中的一个工单传进来
	 * 查找归谁管，然后通过工单编号接单，预约
	 * @param userl
	 * @return
	 * @throws Exception
	 */
	public static void sorting(UserLink userl) throws Exception{
	     
		GetDrPeng gdp=new GetDrPeng();
		//找下社区归谁管
		Staff staff=	selectJson(userl.getCommun());
		//负责人的sid
		String sid =gdp.getSessionId(staff.getId(), staff.getPwd());
		//传入负责人sid和工单id
		logger.info(staff.getId()+"接单："+gdp.chackTask(sid, userl));
	//	System.out.println("\n=========预约时间为接单第二天的晚8点01===========\n");
		logger.info("预约工单："+gdp.appointDate(sid,userl.getTaskid()));
		try{
		SendMail sm=new SendMail();
		sm.sendMail(staff.getMail(), userl.toEmail());
		}catch(Exception e){
			logger.error("邮件发送失败再试一次");
			SendMail sm=new SendMail();
			sm.sendMail(staff.getMail(), userl.toEmail());
		}
		runn();
	}
	/**
	 * 定时器执行的方法
	 */
	public static void runn(){
		try {		
			
			System.out.println("ssssssssssssssssssssssssssssss");
    		 JSONArray    jsonArray= 	json.getUserList();
    		 //拆出来第一个用户负责一遍遍刷
    		 JSONObject user0=jsonArray.getJSONObject(0);
    		 byte[] byteArray = BASE64.decryptBASE64(user0.getString("id"));       
    	     String userName=new String(byteArray);   
    	     String pwd=user0.getString("pas");
    		 //new CCGWBN().run(id,user0.getString("pas"),user0.getString("sq"),user0.getString("email"),true);
    	     GetDrPeng gdp=new GetDrPeng();
    	     String sessionId=  gdp.getSessionId(userName,pwd );
    	     int count   =gdp.getCount(sessionId, userName, pwd);
    	     if(count>=1){
    		 //获取未接工单列表
    		 JSONArray task=	 gdp.getTask(sessionId);
    		 ArrayList<UserLink> userList=gdp.selectArray(task);
    		 //拆出来一个工单
    		 UserLink userl= userList.get(0);
    		 sorting( userl);
    		
    		            	 
    	 }else{
    		 //当工单数为0时进入此else
    	 }
    	 
    	}catch (IOException e) {
    		logger.error("json文件路径错误或不存在/r请检查路径是否为D:\\SDS\\CCGWBN.json");
		} catch(JSONException e){
			logger.error("别犟！！！绝对是json文件配置错了");
		}catch (Exception e) {
		e.printStackTrace();
		}
   
	}
	
}
