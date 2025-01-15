package cn.zvo.sms.service.aliyuncloud;


import cn.zvo.sms.ServiceInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xnx3.BaseVO;


import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送服务
 * @author 陈学斌
 *
 */
public class aliyuncloudService implements ServiceInterface {

	private String regionId; //机房信息

	private String accessKeyId; //Access Key Id

	private String accessKeySecret; //Access Key Secret

	/**
	 * 阿里云短信发送配置参数初始化
	 * @param regionId 机房信息，如
	 * 			<ul>
	 * 				<li>cn-hangzhou</li>
	 * 				<li>cn-qingdao</li>
	 * 				<li>cn-hongkong</li>
	 * 			</ul>
	 * @param accessKeyId Access Key Id ， 参见 https://ak-console.aliyun.com/?spm=#/accesskey
	 * @param accessKeySecret Access Key Secret， 参见 https://ak-console.aliyun.com/?spm=#/accesskey
	 */
	public aliyuncloudService(String regionId, String accessKeyId, String accessKeySecret) {
		this.regionId = regionId;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}

	/**
	 * <b>description</b> :
	 * <p>API 相关</p>
	 *
	 * @param path string Path parameters
	 * @return OpenApi.Params
	 */
	public static com.aliyun.teaopenapi.models.Params createApiInfo() throws Exception {
		com.aliyun.teaopenapi.models.Params params = new com.aliyun.teaopenapi.models.Params()
				// 接口名称
				.setAction("SendSms")
				// 接口版本
				.setVersion("2017-05-25")
				// 接口协议
				.setProtocol("HTTPS")
				// 接口 HTTP 方法
				.setMethod("POST")
				.setAuthType("AK")
				.setStyle("RPC")
				// 接口 PATH
				.setPathname("/")
				// 接口请求体内容格式
				.setReqBodyType("json")
				// 接口响应体内容格式
				.setBodyType("json");
		return params;
	}

	/**
	 * 发送短信接口
	 * @param signName 签名名称
	 * @param templateCode 模版ID
	 * @param templateParams 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。 例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456，传入的字符串为JSON格式
	 * @param phone 手机号
	 * @return 结果
	 */
	@Override
	public BaseVO send(String phone, Map<String, String> params) {

		String signName = params.get("signName");
		String templateCode = params.get("templateCode");
		String templateParam = params.get("templateParam");

		try {
			com.aliyun.teaopenapi.Client client = null;
			// 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
			// 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
			com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
					// 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
					.setAccessKeyId(this.accessKeyId)
					// 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
					.setAccessKeySecret(this.accessKeySecret);
			// Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
			config.endpoint = this.regionId;
			client = new com.aliyun.dysmsapi20170525.Client(config);

			com.aliyun.teaopenapi.models.Params aliyunParams = aliyuncloudService.createApiInfo();
			// query params
			Map<String, Object> queries = new HashMap<>();
			queries.put("PhoneNumbers", phone);
			queries.put("SignName", signName);
			queries.put("TemplateCode", templateCode);
			queries.put("TemplateParam", templateParam);
			// runtime options
			com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
			com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
					.setQuery(com.aliyun.openapiutil.Client.query(queries));
			// 复制代码运行请自行打印 API 的返回值
			// 返回值实际为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
			Map<String, ?> stringMap = client.callApi(aliyunParams, request, runtime);
			for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (key.equals("statusCode")){
					int statusCode = Integer.parseInt(entry.getValue().toString());
					if (statusCode != 200){
						return BaseVO.failure("未收到接口回应，请检查网络是否正常。");
					}
				}

				// 查看是否发送成功
				if (key.equals("body")){
					Gson gson = new Gson();
					JsonObject jsonObject = gson.fromJson(value.toString(), JsonObject.class);
					String code = jsonObject.get("Code").getAsString();
					if (!code.equals("OK")){
						return BaseVO.failure(jsonObject.get("Message").getAsString());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return BaseVO.failure("短信发送失败，请检查参数是否正确。");
		}
		return BaseVO.success();
	}

	@Override
	public BaseVO getBalance() {
		return null;
	}
}
