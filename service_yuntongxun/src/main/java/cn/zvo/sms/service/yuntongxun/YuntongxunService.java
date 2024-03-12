package cn.zvo.sms.service.yuntongxun;


import com.cloopen.rest.sdk.BodyType;
import com.xnx3.BaseVO;


import java.util.HashMap;
import java.util.Set;

/**
 * 短信发送服务
 * @author 陈学斌
 *
 */
public class YuntongxunService {

	private final String serverIp = "app.cloopen.com"; //生产环境请求地址：app.cloopen.com

	private final String serverPort = "8883"; //请求端口

	private String accountSId; //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID

	private String accountToken; //主账号令牌AUTH TOKEN

	private String appId; //请使用管理控制台中已创建应用的APPID

	private String phone; //手机号

	private String templateId; //短信模板ID

	/**
	 * 初始化短信发送
	 * @param accountSId 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID
	 * @param accountToken 主账号令牌AUTH TOKEN
	 * @param appId 请使用管理控制台中已创建应用的APPID
	 * @param phone 手机号
	 * @param templateId 短信模板ID
	 */
	public YuntongxunService(String accountSId,String accountToken,String appId,String phone,String templateId) {
		this.accountSId = accountSId;
		this.accountToken = accountToken;
		this.appId = appId;
		this.phone = phone;
		this.templateId = templateId;
	}
	
	/**
	 * 发送短信接口
	 * @param accountSId 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID
	 * @param accountToken 主账号令牌AUTH TOKEN
	 * @param appId 请使用管理控制台中已创建应用的APPID
	 * @param phone 手机号
	 * @param templateId 短信模板ID
	 * @param code 验证码
	 * @return 结果
	 */
	public BaseVO send(YuntongxunService yuntongxunService) {
		CCPRestSmsSDK sdk = new CCPRestSmsSDK();
		sdk.init(yuntongxunService.serverIp, yuntongxunService.serverPort);
		sdk.setAccount(yuntongxunService.accountSId, yuntongxunService.accountToken);
		sdk.setAppId(yuntongxunService.appId);
		sdk.setBodyType(BodyType.Type_JSON);
		String code = "";
		for(int i=0;i<6;i++){
			int random = (int)(Math.random()*10);
			code += String.valueOf(random);
		}
		String[] datas = {code};
		HashMap<String, Object> result = sdk.sendTemplateSMS(yuntongxunService.phone,yuntongxunService.templateId,datas);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return BaseVO.failure("发送失败");
		}
		return BaseVO.success();
	}

}
