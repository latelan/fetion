/*
* Send message to moblie
* 07.2013 by abel
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.cookie.Cookie;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class SendMsg {
	
	static String m;
	static String pass;
	static String to;
	static String shortMsg = "nothing";
	
	public static void main(String[] args) {
		// 1.request login
		// 2.post the messages
		// 3.send message
		
		if(args.length == 0){
			System.out.println("usage: SendMsg [message] | [addtion] | [sendto]");
			return ;
		}
		else if(args.length > 3){
			System.out.println("usage: SendMsg [message] | [addtion] | [sendto]");
			return ;
		}
		
		if(args.length == 1){
			shortMsg = args[0];
		}
		else {
			shortMsg =args[0];
			if(args[1].equals("200")){
				shortMsg += "/successful";
			}
			else if(args[1].equals("400")){
				shortMsg += "/failed";
			}
		}
		try{
			//System.out.println(to+" "+args[2]);
			String t = Long.toString(System.currentTimeMillis());
			init();
			if(args.length == 3){
				to = args[2];
			}
			
			String host = "http://f.10086.cn/im5/";
			
			DefaultHttpClient client1 = new DefaultHttpClient();
			HttpGet request1 = new HttpGet(host);
			
			HttpResponse response1 = client1.execute(request1);
			if(response1.getStatusLine().getStatusCode() != 200){
				System.out.println("Error: Cannot connect server,try again");
				return ;
			}
			
			CookieStore cookiestore = client1.getCookieStore();
			
			//test
			// BufferedReader rd0 = new BufferedReader(
			// new InputStreamReader(response1.getEntity().getContent(),"UTF8"));
			
			// String line0 ="";
			
			// while((line0 = rd0.readLine()) != null){
			// 	System.out.println(line0);
			// }
			
			
			DefaultHttpClient client2 = new DefaultHttpClient();
			client2.setCookieStore(cookiestore);
			
			String login_url = "http://f.10086.cn/im5/login/loginHtml5.action";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("t",t));
			params.add(new BasicNameValuePair("m",m));
			params.add(new BasicNameValuePair("pass",pass));
			params.add(new BasicNameValuePair("captchaCode",""));
			params.add(new BasicNameValuePair("checkCodeKey","null"));

			HttpPost request2 = new HttpPost(login_url);
			request2.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			
			HttpResponse response2 = client2.execute(request2);
			if(response2.getStatusLine().getStatusCode() != 200){
				System.out.println("Error: Cannot connect server,try again");
				return ;
			}
						
			//add by abel 08.06 2013
			// search the ContactID
			/***********************START************************/
			DefaultHttpClient client5 = new DefaultHttpClient();
			client5.setCookieStore(cookiestore);
			
			if(args.length == 3)
			{
				String search_url = "http://f.10086.cn/im5/index/searchFriendsByQueryKey.action";
				params.clear();
				params.add(new BasicNameValuePair("queryKey",to));
				
				HttpPost request5 = new HttpPost(search_url);
				request5.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				
				HttpResponse response5 = client5.execute(request5);
				
				//jixi Json
				
				String jsonStr = EntityUtils.toString(response5.getEntity());
				//System.out.println(testStr);
				
				// BufferedReader rd0 = new BufferedReader(
				// new InputStreamReader(response5.getEntity().getContent(),"UTF8"));
				
				// String jsonStr ="";
				// String line0;
				
				// while((line0 = rd0.readLine()) != null){
				// 	jsonStr+=line0;
				// }
				
				JSONObject jsonObj = new JSONObject(jsonStr);
				JSONArray jsonArr = (JSONArray) jsonObj.get("contacts");
				jsonObj = (JSONObject) jsonArr.get(0);
				
				to = jsonObj.getString("idContact");
				
				// Scanner read = new Scanner(System.in);
				// read.next();
			}
			/***********************END************************/

			DefaultHttpClient client3 = new DefaultHttpClient();
			client3.setCookieStore(cookiestore);
			
			String sendmsg_url = "http://f.10086.cn/im5/chat/sendNewGroupShortMsg.action";
			params.clear();
			params.add(new BasicNameValuePair("t",t));
			params.add(new BasicNameValuePair("msg",shortMsg));
			params.add(new BasicNameValuePair("touserid",to));
			
			HttpPost request3 = new HttpPost(sendmsg_url);
			request3.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			
			HttpResponse response3 = client3.execute(request3);
			
			if(response3.getStatusLine().getStatusCode() != 200){
				System.out.println("Error: Cannot connect server,try again");
				return ;
			}
			
			DefaultHttpClient client4 = new DefaultHttpClient();
			client4.setCookieStore(cookiestore);
			
			String logout_url = "http://f.10086.cn/im5/index/logoutsubmit.action";
			params.clear();
			params.add(new BasicNameValuePair("t",t));
			HttpPost request4 = new HttpPost(logout_url);
			request4.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			
			HttpResponse response4 = client4.execute(request4);
			
			if(response4.getStatusLine().getStatusCode() == 200){
				System.out.println("send successful");
			}
			else{
				System.out.println("send failed,code:" + response4.getStatusLine().getStatusCode());	
			}
			
		}catch(FileNotFoundException ex){
			System.out.println("Error: Not find config file,ask Admin for help");
			return ;
		}catch (JSONException e) {
			System.out.println("Warning: Cannot find the friendsID,please check moblie number");
			return ;
		}
		catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
	static void init() throws Exception	{
		File file = new File("./_Setting.dat");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String string = "";
		String s = "";
		
		while((s = br.readLine()) != null){
			string += s;
		}
		
		br.close();
		
		/* decode */
		String strDecode = new String(Encrypt.decode(upToLow(string)));
		m = strDecode.substring(0,strDecode.indexOf("\n"));
		String leftStr = strDecode.substring(strDecode.indexOf("\n")+1,strDecode.length());
		pass = leftStr.substring(0,leftStr.indexOf("\n"));
		to = leftStr.substring(leftStr.indexOf("\n")+1,leftStr.length());
	}
	static String upToLow(String string){
		char[] array = string.toCharArray();
		for (int i = 0 ; i < array.length ; ++i) {
			
			char c = array[i];
			
			if(c >= 'A' && c <= 'Z'){
				c += 32;
			}
			else if (c >= 'a' && c <= 'z') {
				c -= 32;
			}
			array[i] = c;
		}
		return new String(array);
	}
}
