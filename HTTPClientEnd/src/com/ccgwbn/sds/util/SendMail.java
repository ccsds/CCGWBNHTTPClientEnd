package com.ccgwbn.sds.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
public class SendMail {
	public void sendMail(String toEmail,String sq){
		
		 // 不要使用SimpleEmail,会出现乱码问题
	     HtmlEmail email = new HtmlEmail();
	     try {
	      // 这里是SMTP发送服务器的名字：，普通qq号只能是smtp.qq.com ；smtp.exmail.qq.com没测试成功
	      email.setHostName("smtp.qq.com");
	      //开启 SSL 加密
	      email.setSSLOnConnect(true);
	      	//设置需要鉴权端口
	      email.setSmtpPort(587);
	      // 字符编码集的设置
	      email.setCharset("utf-8");
	      // 收件人的邮箱
	      email.addTo(toEmail);
	      // 发送人的邮箱
	      email.setFrom("1414369907@qq.com", "天堂小助手");
	      // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和得到的授权码
	      email.setAuthentication("1414369907@qq.com", "hfagmrpliltsheai");
	      email.setSubject("站前中心故障分配系统");
	      // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
	      email.setMsg("请上故障系统接单或者预约，不听话后果自负！\r\n"+sq);
	      // 发送
	      email.send();
	      System.out.println ( "邮件发送成功!" );
	     } catch (EmailException e) {
	      // TODO Auto-generated catch block
	     // e.printStackTrace();
	      System.out.println ( "邮件发送失败!" );
	     }
		
		
	}
}
