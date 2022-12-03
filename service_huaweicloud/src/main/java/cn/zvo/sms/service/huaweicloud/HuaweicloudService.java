package cn.zvo.sms.service.huaweicloud;

import java.util.Map;
import com.xnx3.BaseVO;
import cn.zvo.sms.SmsInterface;

/**
 * 华为云的短信发送服务
 * @author 管雷鸣
 *
 */
public class HuaweicloudService implements SmsInterface{
	private HuaweiSMSUtil huaweiSMSUtil;
	
	/**
	 * 华为云短信发送
	 * 短信开通：	http://huawei.leimingyun.com/
	 * @param appKey 短信应用的appKey
	 * @param appSecret 短信应用的appSecret
	 * @param sender 国内短信签名通道号或国际/港澳台短信通道号
	 * @param signature 短信签名
	 */
	public HuaweicloudService(String appKey, String appSecret, String sender, String signature) {
		this.huaweiSMSUtil = new HuaweiSMSUtil(appKey, appSecret, sender, signature);
	}
	
	/**
	 * 发送短信
	 * @param phone 接收短信的手机号。可以传入 +8618788888888 ，也可以不带+86，接口里面会自动加上
	 * @param params 其map的传参有： 
	 * 		<ul>
	 * 			<li><pre>params.put("templateId", "模板ID");</pre></li>
	 * 			<li><pre>params.put("templateParas", "模板变量");</pre>选填。使用无变量模板时可不用传入此，也或者传入赋空字符串
	 * 				<ul>
	 * 					<li>单变量模板示例:模板内容为"您的验证码是${1}"时,templateParas可填写为"[\"369751\"]"</li>
	 * 					<li>双变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"</li>
	 * 				</ul>
	 * 				模板中的每个变量都必须赋值，且取值不能为空。
	 * 				<br>查看更多模板和变量规范:产品介绍>模板和变量规范
	 * 			</li>
	 * 		</ul>
	 */
	@Override
	public BaseVO send(String phone, Map<String, String> params) {
		String templateId = params.get("templateId");
		String templateParas = params.get("templateParas");
		if(templateParas == null) {
			templateParas = "";
		}
		return this.huaweiSMSUtil.send(phone, templateParas, templateId);
	}

	@Override
	public BaseVO getBalance() {
		return BaseVO.success("-1");
	}
	
}
