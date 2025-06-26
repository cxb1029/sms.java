package cn.zvo.sms.framework.springboot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.xnx3.BaseVO;
import com.xnx3.ScanClassUtil;
import cn.zvo.sms.ServiceInterface;
import cn.zvo.sms.Sms;

/**
 * 短信
 * @author 管雷鸣
 */
@EnableConfigurationProperties(ApplicationConfig.class)
@Configuration
public class SmsUtil implements CommandLineRunner{
	public static Sms sms;
    @Resource
    private ApplicationConfig smsApplicationConfig;

    /**
     * 加载配置 {@link ApplicationConfig} （aplication.properties/yml）文件的配置数据，通过其属性来决定使用何种配置。
     * <br>这个其实就相当于用java代码来动态决定配置
     * @param config
     */
    public void loadConfig(ApplicationConfig config) {
//    	sms.loadConfig(config); //加载application配置
    	if(config == null) {
    		return;
    	}
    	
		if(config.getService() != null) {
			com.xnx3.Log.debug("load sms config by application.properties / yml : "+this.smsApplicationConfig);
			
			for (Map.Entry<String, Map<String, String>> entry : config.getService().entrySet()) {
				//拼接，取该插件在哪个包
				String datasourcePackage = "cn.zvo.sms.service."+entry.getKey();
				List<Class<?>> classList = ScanClassUtil.getClasses(datasourcePackage);
				if(classList.size() == 0) {
					System.err.println("====================");
					System.err.println(" 【【【 ERROR 】】】    ");
					System.err.println(" sms 未发现 "+datasourcePackage +" 这个包存在，请确认pom.xml是否加入了这个 service 支持服务");
					System.err.println("====================");
					continue;
				}else {
					for (int i = 0; i < classList.size(); i++) {
						com.xnx3.Log.debug("class list item : "+classList.get(i).getName());
					}
				}
				
				//搜索继承ServiceInterface接口的
				List<Class<?>> logClassList = ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.sms.ServiceInterface");
				for (int i = 0; i < logClassList.size(); i++) {
					Class logClass = logClassList.get(i);
					com.xnx3.Log.debug("sms service : "+logClass.getName());
					try {
						Object newInstance = logClass.getDeclaredConstructor(Map.class).newInstance(entry.getValue());
						ServiceInterface service = (ServiceInterface) newInstance;
						sms.setServiceInterface(service);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException  | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
    }
    
    /**
     * 获取sms对象
     */
    public static Sms getSms() {
    	if(sms == null) {
    		com.xnx3.Log.debug("Sms -- SmsUtil().init();");
    		try {
				new SmsUtil().run(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return sms;
    }
    
	@Override
	public void run(String... args) throws Exception {
		if(sms == null) {
			sms = new Sms(null);
		}
    	loadConfig(this.smsApplicationConfig); //加载application配置
	}
	

    /**
     * 克隆
     */
	public Sms clone() {
		Sms newSms = new Sms(sms.getServiceInterface());
		BeanUtils.copyProperties(sms, newSms);
		return newSms;
	}
	
	/**
	 * 发送短信
	 * @param phone 接收短信的手机号。
	 * 		<ul>
	 * 			<li>可传入咱国内所用的11位手机号，如 18700000000 </li>
	 * 			<li>可传入带有国家前缀的手机号，如 +8618700000000 （还要看具体用的短信通道是否支持）</li>
	 * 		</ul>
	 * @param params 传入的参数，根据对接的短信服务通道不同，参与也不同。具体参入什么参数，参见具体对接的短信通道的参数说明。
	 * @return {@link BaseVO#getResult()} 得到是否成功发送的结果：
	 * 		<ul>
	 * 			<li> {@link BaseVO#SUCCESS} : 发送成功</li>
	 * 			<li> {@link BaseVO#FAILURE} : 发送失败，可用 {@link BaseVO#getInfo()} 获取失败原因</li>
	 * 		</ul>
	 */
	public static BaseVO send(String phone, Map<String, String> params) {
		return sms.send(phone, params);
	}

}
