package cn.zvo.sms.framework.springboot;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.properties / yml 中的一些相关配置项目
 * @author 管雷鸣
 */
@Component(value = "smsApplicationConfig")
@ConfigurationProperties(prefix = "sms")
public class ApplicationConfig {
	//自定义的短信服务
	private Map<String, Map<String, String>> service;

	public Map<String, Map<String, String>> getService() {
		return service;
	}

	public void setService(Map<String, Map<String, String>> service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return "ApplicationConfig [service=" + service + "]";
	}
	
}