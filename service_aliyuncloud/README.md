java 发送短信，一行代码接入，可任意切换多家短信通道

## 使用
#### 1. pom.xml 中
````
<!-- 短信的核心实现 https://github.com/xnx3/sms.java -->
<dependency> 
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-core</artifactId>
	<version>1.0</version>
</dependency>
<!-- 使用 阿里云的短信服务通道 -->
<dependency>
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-service-aliyuncloud</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. Java代码

````
ServiceInterface service = new aliyuncloudService("regionId", "accessKeyId","accessKeySecret"); //创建短信服务通道
Sms sms = new Sms(service);
Map<String, String> params = new HashMap<String, String>();
params.put("signName", "阿里云短信测试");
params.put("templateCode", "SMS_154950909");
params.put("templateParam", "{\"code\":\"1234\"}");
sms.send(phone, params);//发送短信
````

##### params 参数说明
params 其map的传参有： 

* ```` params.put("signName", "阿里云短信测试"); ```` 
* ```` params.put("templateCode", "SMS_154950909"); ```` 
* ```` params.put("templateParam", "{\"code\":\"1234\"}"); ````
 * 必填。
	* 单变量模板示例:模板内容为"您的验证码是${code}"时,templateParam可填写为 {\"code\":\"1234\"}
	* 多变量模板示例:模板内容为"您有${number}件快递请到${address}领取"时,templateParam可填写为 {\"number\":\"5\",\"address\":\"菜鸟驿站 \"}

模板中的每个变量都必须赋值，且取值不能为空。  
