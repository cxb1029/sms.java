package cn.zvo.sms.service.sioo;

import java.util.HashMap;
import java.util.Map;
import com.xnx3.BaseVO;
import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import cn.zvo.sms.ServiceInterface;

/**
 * sioo短信发送服务
 * @author 管雷鸣
 *
 */
public class SiooService implements ServiceInterface{
	private int uid;
	private String password;
	
	public static HttpsUtil httpsUtil;	//https请求
	public static Map<String, String> headers;	//https请求的hader
	static{
		httpsUtil = new HttpsUtil();
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json;charset=utf-8");
	}
	
	/**
	 * 初始化短信发送
	 * <p>短信费用及开通： <a href="http://sms.leimingyun.com">sms.leimingyun.com</a></p>
	 * @param uid 用户在短信平台的uid（登录用户名）
	 * @param password 用户在短信平台登录的密码
	 */
	public SiooService(String uid, String password) {
		this.uid = Lang.stringToInt(uid, 0);
		this.password = MD5Util.MD5(password);
	}
	
	/**
	 * @param phone 接收短信的手机号
	 * @param params 其map的传参有： 
	 * 		<ul>
	 * 			<li><pre>params.put("content", "短信的内容");</pre> 发送短信的内容。不加签名，比如这里传入“哈哈哈”,那么用户接收到的短信为 <pre>哈哈哈【短信签名】</pre></li>
	 * 		</ul>
	 * @return 结果
	 */
	@Override
	public BaseVO send(String phone, Map<String, String> params) {
		String content = params.get("content").toString();
		if(content == null || content.trim().length() == 0) {
			return BaseVO.failure("请传入要发送短信的内容。 params 参数的设置为 params.put(\"content\", \"短信的内容\");");
		}
		
		return send(phone, content);
	}

	@Override
	public BaseVO getBalance() {
		String param = "{\"uid\":"+this.uid+",\"password\":\""+this.password+"\"}";
		
		HttpResponse hr;
		try {
			hr = httpsUtil.send("https://submit.10690221.com/get/balance", param, headers);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseVO.failure(e.getMessage());
		}
		
		if(hr.getCode() != 200){
			return BaseVO.failure("请求短信接口响应异常，http code : "+hr.getCode());
		}else{
			//响应正常
			String response = hr.getContent();
			response = response.replace("}", ",");	//吧最后一个}替换为 , 方便substring 截取，避免出错
			//取出code来
			String code = StringUtil.subString(response, "\"code\":", ",", 2);
			if(code == null){
				return BaseVO.failure("未能识别响应结果。响应内容："+response);
			}
			if(code.trim().equals("0")){
				//成功，那么拿到  balance  返回
				//取出 balance
				String balance = StringUtil.subString(response, "\"balance\":", ",", 2);
				return BaseVO.success(balance.trim());
			}else{
				//失败，拿到失败原因返回
				
				//取出  msg 
				String msg = StringUtil.subString(response, "\"msg\":\"", "\",", 2);
				return BaseVO.failure(msg);
			}
		}
	}
	

	/**
	 * 发送一条短信。短信内容自己定，不过前缀会加上签名。
	 * @param phone 接收短信的手机号
	 * @param content 发送短信的内容。不加签名，比如这里传入“哈哈哈”,那么用户接收到的短信为 
	 * 		<pre>
	 * 			【雷鸣云】哈哈哈
	 * 		</pre>
	 * @return 其中 {@link BaseVO#getResult()} 为执行状态，是否成功
	 * 		<ul>
	 *	 		<li>{@link BaseVO#SUCCESS} 	：失败,可以通过 {@link BaseVO#getInfo()} 获得失败原因 </li>
	 * 			<li>{@link BaseVO#FAILURE} 	：成功,可以通过 {@link BaseVO#getInfo()} 获得发送的这个短信的消息唯一编号</li>
	 * 		</ul>
	 */
	public BaseVO send(String phone, String content){
		HttpResponse hr = httpsUtil.get("https://submit.10690221.com/send/ordinarykv?uid="+this.uid+"&password="+this.password+"&mobile="+phone+"&msg="+StringUtil.stringToUrl(content));
		
		if(hr.getCode() != 200){
			return BaseVO.failure("请求短信接口响应异常，http code : "+hr.getCode());
		}else{
			//响应正常
			String response = hr.getContent();
			response = response.replace("}", ",");	//吧最后一个}替换为 , 方便substring 截取，避免出错
			//取出code来
			String code = StringUtil.subString(response, "\"code\":", ",", 2);
			if(code == null){
				return BaseVO.failure("未能识别响应结果。响应内容："+response);
			}
			if(code.trim().equals("0")){
				//成功，那么拿到  msgid  返回
				//取出 msgId
				String msgId = StringUtil.subString(response, "\"msgId\":", ",", 2);
				return BaseVO.success(msgId.trim());
			}else{
				//失败，拿到失败原因返回
				
				//取出  msg 
				String msg = StringUtil.subString(response, "\"msg\":\"", "\",", 2);
				return BaseVO.failure(msg);
			}
		}
	}
	
}
