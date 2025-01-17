package cn.zvo.sms.service.aliyuncloud;


import cn.zvo.sms.ServiceInterface;
import cn.zvo.sms.Sms;
import com.xnx3.BaseVO;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;


/**
 * 示例
 * @author 陈学斌
 *
 */
public class Demo {
	public static void main(String[] args) {
		// 机房信息
		String regionId = "dysmsapi.aliyuncs.com";
		// Access Key Id
		String accessKeyId = "LTAI5tEFkuJukqdrj*******";
		// Access Key Secret
		String accessKeySecret = "w1oowrdKsAlM3lIQhxeRdBG******";
		//手机号
		String phone = "158********";

		Map<String, String> map = new HashedMap();
		map.put("regionId",regionId);
		map.put("accessKeyId",accessKeyId);
		map.put("accessKeySecret",accessKeySecret);
		ServiceInterface service = new AliyuncloudService(map); //创建短信服务通道
		Sms sms = new Sms(service);
		Map<String, String> params = new HashMap<String, String>();
		params.put("signName", "阿里云短信测试");
		params.put("templateCode", "SMS_154950909");
		params.put("templateParam", "{\"code\":\"1234\"}");
		BaseVO send = sms.send(phone, params);//发送短信
		System.out.println(send);
	}
}
