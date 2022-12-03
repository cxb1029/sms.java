package cn.zvo.sms.service.huaweicloud;

import java.util.HashMap;
import java.util.Map;
import cn.zvo.sms.Sms;
import cn.zvo.sms.ServiceInterface;

/**
 * 示例
 * @author 管雷鸣
 *
 */
public class Demo {
	public static void main(String[] args) {
		ServiceInterface service = new HuaweicloudService("appKey", "appSecret ", "sender", "signature"); //创建短信服务通道
		Sms sms = new Sms(service); //创建短信发送类，指定使用哪个短信服务通道来实现短信发送
		Map<String, String> params = new HashMap<String, String>();
		params.put("templateId", "模板ID");
		params.put("templateParas", "[\"390673\"]");
		sms.send("17000000000", params); //发送短信
	}
}
