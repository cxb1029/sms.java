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
<!-- 使用 容联云的短信服务通道 -->
<dependency>
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-service-yuntongxun</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. Java代码

````
ServiceInterface service = new YuntongxunService("accountSId", "accountToken","appId"); //创建短信服务通道
Sms sms = new Sms(service);
Map<String, String> params = new HashMap<String, String>();
params.put("templateId", "模板ID");
params.put("templateParas", "[\"390673\"]");
sms.send(phone, params); //发送短信
````

##### params 参数说明
params 其map的传参有： 


* ```` params.put("templateId", "模板ID"); ```` 
* ```` params.put("templateParas", "[\"390673\"]"); ````
 * 必填。
	* 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParams可填写为 [\"390673\"]
	* 多变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParams可填写为 ["3","人民公园正门"]

模板中的每个变量都必须赋值，且取值不能为空。  
