package com.ccgwbn.sds.util;

import java.util.Base64;


public class BASE64 {
    
    /**     
     * BASE64解密    
   * @param key           
     * @return           
     * @throws Exception           
     */                
    public static byte[] decryptBASE64(String key) throws Exception {                 
      
    	
    	
    	
    	return Base64.getDecoder().decode(key);   
    }                 
                    
    /**          
     * BASE64加密    
   * @param key           
     * @return           
     * @throws Exception           
     */                
    public static String encryptBASE64(byte[] key) throws Exception {  
    
       		return Base64.getEncoder().encodeToString(key);
            
    }         
           
}
