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
	
	// �����ʼ����˺�
    public static String ownEmailAccount = "liudanyi_13579@163.com";
    // �����ʼ�������------����Ȩ��
    public static String ownEmailPassword = "ldy888";
    // �����ʼ���smtp ������ ��ַ
    public static String myEmailSMTPHost = "smtp.163.com";
    // �����ʼ��Է�������
    public static String receiveMailAccount = "1524523954@qq.com";
	
		
	   public static void main(String[] args) {
		   
		   Properties prop = new Properties();
	        // �����ʼ�������õ�Э��smtp
	        prop.setProperty("mail.transport.protocol", "smtp");
	        // ���÷������ʼ���������smtp��ַ
	        // ���������׵�����smtp��������ַΪ��
	        prop.setProperty("mail.smtp.host", myEmailSMTPHost);
	        // ������֤����
	        prop.setProperty("mail.smtp.auth", "true");

	        // SMTP �������Ķ˿� (�� SSL ���ӵĶ˿�һ��Ĭ��Ϊ 25, ���Բ����, ��������� SSL ����,
	        // ��Ҫ��Ϊ��Ӧ����� SMTP �������Ķ˿�, ����ɲ鿴��Ӧ�������İ���,
	        // QQ�����SMTP(SLL)�˿�Ϊ465��587, ������������ȥ�鿴)

	        /*final String smtpPort = "465";
	        prop.setProperty("mail.smtp.port", smtpPort);
	        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
	        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);*/

	        // ��������ػ�������������
	        Session session = Session.getInstance(prop);
	        // �Ự����debugģʽ
	        session.setDebug(true);
	        // �����ʼ�����
	        try {
				Message message = createSimpleMail(session);

				Transport trans = session.getTransport();
				// �����ʼ�������
				trans.connect(ownEmailAccount, ownEmailPassword);
				// ������Ϣ
				trans.sendMessage(message, message.getAllRecipients());
				// �ر�����
				trans.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	   }
	   
	   
	   /**  
	    * @Title: createSimpleMail  
	    * @Description: �����ʼ�����
	    * @author: chengpeng 
	    * @param @param session
	    * @param @return
	    * @param @throws Exception    �趨�ļ�  
	    * @return Message    ��������  
	    * @throws  
	    */
	    public static Message createSimpleMail(Session session) throws Exception {
	    	 // ����Ԥ������
	    	 String url ="http://t.weather.sojson.com/api/weather/city/101080501";
	         String json= Test02.getHttpResponse(url);
	         System.out.println(json);
	         //���������
	         JSONObject jintiantianqi = Emailsendweather.jintiantianqi();
	        MimeMessage message = new MimeMessage(session);
	        // ���÷����ʼ���ַ,param1 �����͵�ַ param2 �����͵�����(�����) param3 �������Ʊ��뷽ʽ
	        message.setFrom(new InternetAddress("liudanyi_13579@163.com", "Mr_liu", "utf-8"));
	        // �����ռ���
	        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount, "����", "utf-8"));
	        // To: �����ռ��ˣ���ѡ��
	        /*message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
	        // Cc: ���ͣ���ѡ��
	        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
	        // Bcc: ���ͣ���ѡ��
	        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));*/
	        // �����ʼ�����
	        message.setSubject("���Է����ʼ�");
	        // �����ʼ�����
	        message.setContent("<body style=\\\"background-color:#FFB6C1;color: #fff;font-weight: bolder;\\\">\r\n" + 
	        		"<br/>\r\n" + 
	        		"<center>\r\n" + 
	        		"    <br>\r\n" + 
	        		"    <h3>������������Ԥ��</h3>\r\n" + 
	        		"    <p>�����ǣ�"+jintiantianqi.getString("type")+"��</p>\r\n" + 
	        		"    <p>����¶�Ϊ��"+jintiantianqi.getString("high")+"</p>\r\n" + 
	        		"    <p>����¶�Ϊ��"+jintiantianqi.getString("low")+"</p>\r\n" + 
	        		"    <p>����ָ��Ϊ��"+jintiantianqi.getString("aqi")+"</p>\r\n" + 
	        		"    <p>����Ϊ��"+jintiantianqi.getString("fx")+"</p>\r\n" + 
	        		"    <p>������"+jintiantianqi.getString("fl")+"</p>\r\n" + 
	        		"    <h3>�����װ���˵����"+jintiantianqi.getString("notice")+" </h3>\r\n" + 
	        		"</center>\r\n" + 
	        		"</body>", "text/html;charset=utf-8");
	        // ���÷���ʱ��
	        message.setSentDate(new Date());
	        // ��������ı༭����
	        message.saveChanges();
	        // �����洴���Ķ���д�뱾��
	        //OutputStream out = new FileOutputStream("MyEmail.eml");
	        //message.writeTo(out);
	        //out.flush();
	        //out.close();
	        return message;

	    }
	    /**
	     * 
	     * @return  �������������
	     */
	    public static JSONObject jintiantianqi() {
	    	String url ="http://t.weather.sojson.com/api/weather/city/101180901";
	         String json= Emailsendweather.getHttpResponse(url);
	         Map result = JSONObject.parseObject(json, Map.class);
	         //��JSONת����Map����
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
	    
	    //���Ҹ�JSON�Ҹ�������һ�²��������س�String
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
	            //��ȡURL����Ӧ
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
	
