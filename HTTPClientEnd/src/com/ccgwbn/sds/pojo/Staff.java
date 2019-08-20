package com.ccgwbn.sds.pojo;


public class Staff {
private String id;
private String pwd;
private String sq;
private String mail;
public Staff(String id,String pwd,String sq,String mail){
	this.id=id;
	this.pwd=pwd;
	this.sq=sq;
	this.mail=mail;
}
public Staff(){
	
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
public String getSq() {
	return sq;
}
public void setSq(String sq) {
	this.sq = sq;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}
@Override
public String toString() {
	return "Staff [id=" + id + ", pwd=" + pwd + ", sq=" + sq + ", mail=" + mail + "]";
}

}
