package cn.zvo.sms.service.yuntongxun;


import cn.zvo.sms.ServiceInterface;
import com.cloopen.rest.sdk.BodyType;
import com.xnx3.BaseVO;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 短信发送服务
 * @author 陈学斌
 *
 */
public class YuntongxunService implements ServiceInterface {

	private final String serverIp = "app.cloopen.com"; //生产环境请求地址：app.cloopen.com

	private final String serverPort = "8883"; //请求端口

	private String accountSId; //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID

	private String accountToken; //主账号令牌AUTH TOKEN

	private String appId; //请使用管理控制台中已创建应用的APPID

	/**
	 * 初始化短信发送
	 * @param accountSId 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID
	 * @param accountToken 主账号令牌AUTH TOKEN
	 * @param appId 请使用管理控制台中已创建应用的APPID
	 */
	public YuntongxunService(String accountSId,String accountToken,String appId) {
		this.accountSId = accountSId;
		this.accountToken = accountToken;
		this.appId = appId;
	}

	/**
	 * 发送短信接口
	 * @param accountSId 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID
	 * @param accountToken 主账号令牌AUTH TOKEN
	 * @param appId 请使用管理控制台中已创建应用的APPID
	 * @param phone 手机号
	 * @param templateId 短信模板ID
	 * @return 结果
	 */
	@Override
	public BaseVO send(String phone, Map<String, String> params) {
		String templateId = params.get("templateId");
		String templateParams = params.get("templateParams");
		CCPRestSmsSDK sdk = new CCPRestSmsSDK();
		sdk.init(this.serverIp, this.serverPort);
		sdk.setAccount(this.accountSId, this.accountToken);
		sdk.setAppId(this.appId);
		sdk.setBodyType(BodyType.Type_JSON);
		String[] datas = {templateParams};
		HashMap<String, Object> result = sdk.sendTemplateSMS(phone,templateId,datas);
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

	@Override
	public BaseVO getBalance() {
		return null;
	}
}
