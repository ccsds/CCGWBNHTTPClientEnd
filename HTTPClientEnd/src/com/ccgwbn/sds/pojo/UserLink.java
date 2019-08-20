package com.ccgwbn.sds.pojo;

public class UserLink {
	//INSTANCEID
	private String taskid;
	//工单编号
	private String orderno;
	//社区
	private String commun;
	//详细地址
	private String link;
	//电话号
	private String tal;
	//用户名
	private String linkMan;
	//账号
	private String  customerno;
	public UserLink(){
		
	}
	public UserLink(String taskid, String orderno, String commun, String link,
			String sheq, String tal, String linkMan, String customerno) {
		super();
		this.taskid = taskid;
		this.orderno = orderno;
		this.commun = commun;
		this.link = link;
		this.tal = tal;
		this.linkMan = linkMan;
		this.customerno = customerno;
	}
	@Override
	public String toString() {
		return 	", 工单编号=" + orderno +
				", 用户名=" + linkMan +
				", 账号=" + customerno +
				", 社区="+ commun +
				", 地址=" + link +  
				", 电话=" + tal+  
				", User [taskid=" + taskid + 
				"]";
	}
	public String toEmail(){
		return 	
				" 用户名=" + linkMan +
				", 账号=" + customerno +
				", 社区="+ commun +
				", 地址=" + link +  
				", 电话=" + tal 
				;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getCommun() {
		return commun;
	}
	public void setCommun(String commun) {
		this.commun = commun;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}


	public String getTal() {
		return tal;
	}
	public void setTal(String tal) {
		this.tal = tal;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
}
