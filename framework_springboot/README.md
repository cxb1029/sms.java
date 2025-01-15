springboot 项目中使用  

## 1. 快速使用

#### 1.1 pom.xml 加入

````
<!-- 日志的核心支持 https://github.com/xnx3/sms.java -->
<dependency> 
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-core</artifactId>
	<version>1.0</version>
</dependency>
<!-- 在 SpringBoot 框架中的快速使用。 （在不同的框架中使用，这里引入的framework.xxx也不同） -->
<dependency> 
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-framework-springboot</artifactId>
	<version>1.0</version>
</dependency> 
<!-- 加入华为云的短信通道  （使用哪种短信通道，这里artifactId就引入的哪里的 service_xxx -->
<dependency>
	<groupId>cn.zvo.sms</groupId>
	<artifactId>sms-service-huaweicloud</artifactId>
	<version>1.0</version>
</dependency>
````

#### 1.2 application.properties 配置

````
#
# 这里的huaweicloud 便是 service_huaweicloud 中的 huaweicloud
# 这样配置后， SmsUtil 便会自动使用 huaweicloud 的短信服务
#
sms.service.huaweicloud.appKey=xxxxx
sms.service.huaweicloud.appSecret=xxxxx
sms.service.huaweicloud.sender=xxxxx
sms.service.huaweicloud.signature=xxxxx
````


#### 1.3 Java 中使用

````
Map<String, String> params = new HashMap<String, String>();
params.put("templateId", "模板ID");
params.put("templateParams", "[\"390673\"]");
SmsUtil.send("17000000000", params); //发送短信
````
