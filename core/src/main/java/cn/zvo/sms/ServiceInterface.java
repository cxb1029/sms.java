package cn.zvo.sms;

import java.util.Map;
import com.xnx3.BaseVO;

/**
 * 短信服务的接口
 * @author 管雷鸣
 */
public interface ServiceInterface {
	
	/**
	 * 发送短信
	 * @param phone 接收短信的手机号。
	 * 		<ul>
	 * 			<li>可传入咱国内所用的11位手机号，如 18700000000 </li>
	 * 			<li>可传入带有国家前缀的手机号，如 +8618700000000</li>
	 * 		</ul>
	 * @param params 传入的参数，根据对接的短信服务通道不同，参与也不同。具体参入什么参数，参见具体对接的短信通道的参数说明。
	 * @return {@link BaseVO#getResult()} 得到是否成功发送的结果：
	 * 		<ul>
	 * 			<li> {@link BaseVO#SUCCESS} : 发送成功</li>
	 * 			<li> {@link BaseVO#FAILURE} : 发送失败，可用 {@link BaseVO#getInfo()} 获取失败原因</li>
	 * 		</ul>
	 */
	public BaseVO send(String phone, Map<String, String> params);

	/**
	 * 获取自己当前的短信余额，短信还剩多少条
	 * @return 其中 {@link BaseVO#getResult()} 为执行状态，是否成功
	 * 		<ul>
	 *	 		<li>{@link BaseVO#SUCCESS} 	：失败,可以通过 {@link BaseVO#getInfo()} 获得失败原因 </li>
	 * 			<li>{@link BaseVO#FAILURE} 	：成功,可以通过 {@link BaseVO#getInfo()} 获得短信剩余条数</li>
	 * 		</ul>
	 * 		有的像是华为云等云平台，是按实际发送的量来收费的，也就是无剩余条数限制，此种场景 info 参数可以直接返回-1
	 */
	public BaseVO getBalance();
	
}
