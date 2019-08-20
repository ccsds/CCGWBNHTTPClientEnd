package com.ccgwbn.sds.util;
import java.util.Base64;

/**
 * 
 * 试试jdk1.8中如何进行base64加密解密
 *  * @author Administrator
 *
 */


public class jd8base {
    private static final String TEST_STRING = "0123456789，0123456789，0123456789，0123456789，0123456789，0123456789，0123456789";

	public static void main(String[] args){
		
        System.out.println(base64En(TEST_STRING));
        System.out.println(new String(base64De(base64En(TEST_STRING))));
	}
	
	public static byte[] base64De(String key){
		
		return Base64.getDecoder().decode(key);
	}
	public static String base64En(String key){
		
		return Base64.getEncoder().encodeToString(key.getBytes());
	}
}
