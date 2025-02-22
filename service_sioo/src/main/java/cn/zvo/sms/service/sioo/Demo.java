package cn.zvo.sms.service.sioo;

import java.util.HashMap;
import java.util.Map;

import cn.zvo.sms.Sms;
import cn.zvo.sms.ServiceInterface;
import org.apache.commons.collections.map.HashedMap;

/**
 * 示例
 * @author 管雷鸣
 *
 */
public class Demo {
	public static void main(String[] args) {
		String uid = "uid";
		String password = "password";
		Map<String, String> map = new HashedMap();
		map.put("uid",uid);
		map.put("password",password);
		ServiceInterface service = new SiooService(map); //创建短信服务通道
		Sms sms = new Sms(service); //创建短信发送类，指定使用哪个短信服务通道来实现短信发送
		Map<String, String> params = new HashMap<String, String>();
		params.put("content", "你好，我是短信的内容");
		sms.send("17000000000", params); //发送短信
	}
}
