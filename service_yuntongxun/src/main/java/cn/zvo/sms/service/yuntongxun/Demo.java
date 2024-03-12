package cn.zvo.sms.service.yuntongxun;


import com.xnx3.BaseVO;




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
		//模板ID
		String templateId = "templateId";
		YuntongxunService yuntongxunService = new YuntongxunService(accountSId, accountToken, appId, phone, templateId);
		BaseVO baseVO = yuntongxunService.send(yuntongxunService);
	}
}
