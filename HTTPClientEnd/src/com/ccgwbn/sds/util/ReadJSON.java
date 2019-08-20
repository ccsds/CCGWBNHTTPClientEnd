package com.ccgwbn.sds.util;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ReadJSON {

	/**
	 * 通过io流读取
	 * @throws IOException 
	 */
 public JSONArray getUserList() throws IOException{
	   //定义指定磁盘的文件的File对象
     File file = new File("D:\\SDS\\CCGWBN.JSON");
     //存放文件字符的字符串
     String str="";
     //返回jsonArray
     JSONArray jsonArr=null;
     JSONObject json;
     if(! file.exists()){
         System.out.println("对不起，不包含指定路径的文件D:\\SDS\\CCGWBN.json");
     }else{
         //根据指定路径的File对象创建FileReader对象
            FileReader fr = new FileReader(file);
             char[] data = new char[23];         //定义char数组
             int length = 0;
           //循环读取文件中的数据
             while((length = fr.read(data))>0){  
            	 //将读取文件的内容+=String 对象
             	str+= new String(data,0,length);        
                              //输出读取内容
             } 
             //关流
             fr.close();                             //关闭流
             //json
             
             json    = JSONObject.fromObject(str);
 	    	jsonArr=JSONArray.fromObject(json.getJSONArray("list"));
 	       	
 	    		
	}
     return jsonArr;
 }
		

        
	

}
