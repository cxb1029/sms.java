package cn.zvo.sms.service.yuntongxun;


import cn.zvo.sms.ServiceInterface;
import cn.zvo.sms.Sms;

import java.util.HashMap;
import java.util.Map;


/**
 * 示例
 * @author 陈学斌
 *
 */
public class Demo {
	public static void main(String[] args) {
		//主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
		String accountSId = "accountSId";
		String accountToken = "auth token";
		//请使用管理控制台中已创建应用的APPID
		String appId = "appId";
		//手机号
		String phone = "1352*******";

		ServiceInterface service = new YuntongxunService(accountSId, accountToken,appId); //创建短信服务通道
		Sms sms = new Sms(service);
		Map<String, String> params = new HashMap<String, String>();
		params.put("templateId", "模板ID");
		params.put("templateParas", "[\"390673\"]");
		sms.send(phone, params); //发送短信
	}
}
