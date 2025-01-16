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
<!-- 使用 华为云的短信服务通道 -->
<dependency>
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-service-huaweicloud</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. Java代码

````
String appKey = "appKey";
String appSecret = "appSecret";
String sender = "sender";
String signature = "signature";
Map<String, String> map = new HashedMap();
map.put("appKey",appKey);
map.put("appSecret",appSecret);
map.put("sender",sender);
map.put("signature",signature);
ServiceInterface service = new HuaweicloudService(map); //创建短信服务通道
Sms sms = new Sms(service); //创建短信发送类，指定使用哪个短信服务通道来实现短信发送
Map<String, String> params = new HashMap<String, String>();
params.put("templateId", "模板ID");
params.put("templateParams", "[\"390673\"]");
sms.send("17000000000", params); //发送短信
````

##### params 参数说明
params 其map的传参有： 

* ```` params.put("templateId", "模板ID"); ```` 
* ```` params.put("templateParams", "模板变量"); ```` 选填。使用无变量模板时可不用传入此，也或者传入赋空字符串
	* 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParams可填写为 ["369751"]
	* 多变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParams可填写为 ["3","人民公园正门"]

模板中的每个变量都必须赋值，且取值不能为空。  
查看更多模板和变量规范:产品介绍>模板和变量规范
