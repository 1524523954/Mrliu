package com.yuntu.pojo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Emailsendweather {
	
	// 发送邮件的账号
    public static String ownEmailAccount = "liudanyi_13579@163.com";
    // 发送邮件的密码------》授权码
    public static String ownEmailPassword = "ldy888";
    // 发送邮件的smtp 服务器 地址
    public static String myEmailSMTPHost = "smtp.163.com";
    // 发送邮件对方的邮箱
    public static String receiveMailAccount = "1524523954@qq.com";
	
		
	   public static void main(String[] args) {
		   
		   Properties prop = new Properties();
	        // 设置邮件传输采用的协议smtp
	        prop.setProperty("mail.transport.protocol", "smtp");
	        // 设置发送人邮件服务器的smtp地址
	        // 这里以网易的邮箱smtp服务器地址为例
	        prop.setProperty("mail.smtp.host", myEmailSMTPHost);
	        // 设置验证机制
	        prop.setProperty("mail.smtp.auth", "true");

	        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
	        // 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
	        // QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)

	        /*final String smtpPort = "465";
	        prop.setProperty("mail.smtp.port", smtpPort);
	        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
	        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);*/

	        // 创建对象回话跟服务器交互
	        Session session = Session.getInstance(prop);
	        // 会话采用debug模式
	        session.setDebug(true);
	        // 创建邮件对象
	        try {
				Message message = createSimpleMail(session);

				Transport trans = session.getTransport();
				// 链接邮件服务器
				trans.connect(ownEmailAccount, ownEmailPassword);
				// 发送信息
				trans.sendMessage(message, message.getAllRecipients());
				// 关闭链接
				trans.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	   }
	   
	   
	   /**  
	    * @Title: createSimpleMail  
	    * @Description: 创建邮件对象
	    * @author: chengpeng 
	    * @param @param session
	    * @param @return
	    * @param @throws Exception    设定文件  
	    * @return Message    返回类型  
	    * @throws  
	    */
	    public static Message createSimpleMail(Session session) throws Exception {
	    	 // 天气预报内容
	    	 String url ="http://t.weather.sojson.com/api/weather/city/101080501";
	         String json= Test02.getHttpResponse(url);
	         System.out.println(json);
	         //今天的天气
	         JSONObject jintiantianqi = Emailsendweather.jintiantianqi();
	        MimeMessage message = new MimeMessage(session);
	        // 设置发送邮件地址,param1 代表发送地址 param2 代表发送的名称(任意的) param3 代表名称编码方式
	        message.setFrom(new InternetAddress("liudanyi_13579@163.com", "Mr_liu", "utf-8"));
	        // 代表收件人
	        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount, "李四", "utf-8"));
	        // To: 增加收件人（可选）
	        /*message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
	        // Cc: 抄送（可选）
	        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
	        // Bcc: 密送（可选）
	        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));*/
	        // 设置邮件主题
	        message.setSubject("测试发送邮件");
	        // 设置邮件内容
	        message.setContent("<body style=\\\"background-color:#FFB6C1;color: #fff;font-weight: bolder;\\\">\r\n" + 
	        		"<br/>\r\n" + 
	        		"<center>\r\n" + 
	        		"    <br>\r\n" + 
	        		"    <h3>明天洛阳天气预报</h3>\r\n" + 
	        		"    <p>明天是："+jintiantianqi.getString("type")+"天</p>\r\n" + 
	        		"    <p>最高温度为："+jintiantianqi.getString("high")+"</p>\r\n" + 
	        		"    <p>最低温度为："+jintiantianqi.getString("low")+"</p>\r\n" + 
	        		"    <p>空气指数为："+jintiantianqi.getString("aqi")+"</p>\r\n" + 
	        		"    <p>风向为："+jintiantianqi.getString("fx")+"</p>\r\n" + 
	        		"    <p>风力："+jintiantianqi.getString("fl")+"</p>\r\n" + 
	        		"    <h3>你最亲爱的说：，"+jintiantianqi.getString("notice")+" </h3>\r\n" + 
	        		"</center>\r\n" + 
	        		"</body>", "text/html;charset=utf-8");
	        // 设置发送时间
	        message.setSentDate(new Date());
	        // 保存上面的编辑内容
	        message.saveChanges();
	        // 将上面创建的对象写入本地
	        //OutputStream out = new FileOutputStream("MyEmail.eml");
	        //message.writeTo(out);
	        //out.flush();
	        //out.close();
	        return message;

	    }
	    /**
	     * 
	     * @return  今天的天气对象
	     */
	    public static JSONObject jintiantianqi() {
	    	String url ="http://t.weather.sojson.com/api/weather/city/101180901";
	         String json= Emailsendweather.getHttpResponse(url);
	         Map result = JSONObject.parseObject(json, Map.class);
	         //将JSON转换成Map对象
	         String cityInfo = result.get("cityInfo").toString();
	         String data = result.get("data").toString();
	         Map cityInfoMap = JSONObject.parseObject(cityInfo, Map.class);
	         Map dataMap = JSONObject.parseObject(data, Map.class);
	         String yesterday = dataMap.get("yesterday").toString();
	         String forecast = dataMap.get("forecast").toString();
	         JSONArray parseArray = JSONObject.parseArray(forecast);
	         JSONObject jsonObject = parseArray.getJSONObject(0);
	         return jsonObject;
	    }
	    
	    //给我个JSON我给您解析一下并给您返回成String
	    public static String getHttpResponse(String allConfigUrl) {
	        BufferedReader in = null;
	        StringBuffer result = null;
	        try {
	             
	            URI uri = new URI(allConfigUrl);
	            URL url = uri.toURL();
	            URLConnection connection = url.openConnection();
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setRequestProperty("Charset", "utf-8");
	         
	            connection.connect();
	             
	            result = new StringBuffer();
	            //读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream(),"UTF-8"));
	            String line;
	            
	            while ((line = in.readLine()) != null) {
	            	
	                result.append(line);
	            }
	             
	            return result.toString();
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return null;
	    }

	}
	
