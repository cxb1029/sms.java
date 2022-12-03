java 发送短信，一行代码接入，可任意切换多家短信通道

## 快速使用
#### 1. pom.xml 中
````
<!-- 短信的核心实现 https://github.com/xnx3/sms.java -->
<dependency> 
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-core</artifactId>
	<version>1.0</version>
</dependency>
<!-- 使用 sioo的短信服务通道 -->
<dependency>
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-service-sioo</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. Java代码

````
ServiceInterface service = new SiooService("12345", "password"); //创建短信服务通道
Sms sms = new Sms(service); //创建短信发送类，指定使用哪个短信服务通道来实现短信发送
Map<String, String> params = new HashMap<String, String>(); //构造发送短信的参数。这里不同的短信服务通道所传入的params参数是不同的，详细参见具体短信服务通道的使用说明
params.put("content", "你好，我是短信的内容");
sms.send("17000000000", params); //发送短信
````

这里创建Sms对象时使用的是 SiooService 短信服务通道，你也可以使用别的短信服务通道，如 HuaweicloudService  
另外不同的通道在 ```` sms.send(...) ```` 发送短信时传入的map格式的params参数也不同，具体还要看使用的是哪个短信通道

## 短信服务通道
短信服务通道列表及其详细使用说明，可参考：  
* [sioo 短信服务通道](./service_sioo/)
* [华为云 短信服务通道](./service_huaweicloud/)
