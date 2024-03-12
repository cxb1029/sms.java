package cn.zvo.sms.service.yuntongxun;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.utils.DateUtil;
import com.cloopen.rest.sdk.utils.HttpClientUtil;
import com.cloopen.rest.sdk.utils.ParmUtils;
import com.cloopen.rest.sdk.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * yuntongxun短信发送服务
 * @author 陈学斌
 *
 */
public class CCPRestSmsSDK  {
	private final Log log = LogFactory.getLog(CCPRestSmsSDK.class);
	private static final String TemplateSMS = "SMS/TemplateSMS";
	private static final String AcountType = "Accounts";
	private String SERVER_IP;
	private String SERVER_PORT;
	private String ACCOUNT_SID;
	private String ACCOUNT_TOKEN;
	private String App_ID;
	private BodyType BODY_TYPE;
	private Boolean USE_SSL;

	public CCPRestSmsSDK() {
		this.BODY_TYPE = BodyType.Type_JSON;
		this.USE_SSL = true;
	}

	public void setBodyType(BodyType bodyType) {
		this.BODY_TYPE = bodyType;
	}

	public void init(String serverIP, String serverPort) {
		this.init(serverIP, serverPort, true);
	}

	public void init(String serverIP, String serverPort, Boolean useSSL) {
		if (!StringUtils.isEmpty(serverIP) && !StringUtils.isEmpty(serverPort)) {
			this.SERVER_IP = serverIP;
			this.SERVER_PORT = serverPort;
			this.USE_SSL = useSSL;
		} else {
			this.log.error("初始化异常:serverIP或serverPort为空");
			throw new IllegalArgumentException("必选参数:" + (StringUtils.isEmpty(serverIP) ? " 服务器地址 " : "") + (StringUtils.isEmpty(serverPort) ? " 服务器端口 " : "") + "为空");
		}
	}

	public void setAccount(String accountSid, String accountToken) {
		if (!StringUtils.isEmpty(accountSid) && !StringUtils.isEmpty(accountToken)) {
			this.ACCOUNT_SID = accountSid;
			this.ACCOUNT_TOKEN = accountToken;
		} else {
			this.log.error("初始化异常:accountSid或accountToken为空");
			throw new IllegalArgumentException("必选参数:" + (StringUtils.isEmpty(accountSid) ? " 主帐号名称" : "") + (StringUtils.isEmpty(accountToken) ? " 主帐号令牌 " : "") + "为空");
		}
	}

	public void setAppId(String appId) {
		if (StringUtils.isEmpty(appId)) {
			this.log.error("初始化异常:appId为空");
			throw new IllegalArgumentException("必选参数: 应用Id 为空");
		} else {
			this.App_ID = appId;
		}
	}

	public HashMap<String, Object> sendTemplateSMS(String to, String templateId, String[] datas) {
		return this.send(to, templateId, datas, (String)null, (String)null);
	}

	public HashMap<String, Object> sendTemplateSMS(String to, String templateId, String[] datas, String subAppend, String reqId) {
		return this.send(to, templateId, datas, subAppend, reqId);
	}

	private HashMap<String, Object> send(String to, String templateId, String[] datas, String subAppend, String reqId) {
		HashMap<String, Object> validate = this.accountValidate();
		if (validate != null) {
			return validate;
		} else if (!StringUtils.isEmpty(to) && !StringUtils.isEmpty(this.App_ID) && !StringUtils.isEmpty(templateId)) {
			String timestamp = DateUtil.dateToStr(new Date(), 5);
			String sig = ParmUtils.generateSig(this.ACCOUNT_SID, this.ACCOUNT_TOKEN, timestamp);
			String authorization = "";

			try {
				authorization = ParmUtils.generateAuthorization(this.ACCOUNT_SID, timestamp);
			} catch (UnsupportedEncodingException var15) {
				var15.printStackTrace();
				throw new RuntimeException("生成authorization异常" + var15.getMessage());
			}

			String url = this.getBaseUrl().append("/Accounts/").append(this.ACCOUNT_SID).append("/SMS/TemplateSMS?sig=").append(sig).toString();
			String requsetbody = "";
			if (this.BODY_TYPE == BodyType.Type_JSON) {
				requsetbody = this.generateJson(to, templateId, datas, subAppend, reqId);
			} else {
				requsetbody = this.generateXml(to, templateId, datas, subAppend, reqId);
			}

			this.log.info("sendTemplateSMS Request url:" + url);
			this.log.info("sendTemplateSMS Request body:" + requsetbody);
			String result = HttpClientUtil.post(url, authorization, requsetbody, this.BODY_TYPE, "utf-8");
			if (result != null && result != "") {
				try {
					return this.BODY_TYPE == BodyType.Type_JSON ? this.jsonToMap(result) : this.xmlToMap(result);
				} catch (Exception var14) {
					return this.getMyError("172003", "返回包体错误");
				}
			} else {
				return this.getMyError("172001", "网络错误");
			}
		} else {
			throw new IllegalArgumentException("必填参数:" + (StringUtils.isEmpty(to) ? " 手机号码 " : "") + (StringUtils.isEmpty(templateId) ? " 模板Id " : "") + "为空");
		}
	}

	private HashMap<String, Object> jsonToMap(String result) {
		HashMap<String, Object> hashMap = new HashMap();
		JsonObject asJsonObject = JsonParser.parseString(result).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entrySet = asJsonObject.entrySet();
		HashMap<String, Object> hashMap2 = new HashMap();
		Iterator var6 = entrySet.iterator();

		while(var6.hasNext()) {
			Map.Entry<String, JsonElement> m = (Map.Entry)var6.next();
			if (!"statusCode".equals(m.getKey()) && !"statusMsg".equals(m.getKey())) {
				if (!"SubAccount".equals(m.getKey()) && !"totalCount".equals(m.getKey()) && !"token".equals(m.getKey()) && !"downUrl".equals(m.getKey())) {
					JsonObject asJsonObject2 = JsonParser.parseString(((JsonElement)m.getValue()).toString()).getAsJsonObject();
					Set<Map.Entry<String, JsonElement>> entrySet2 = asJsonObject2.entrySet();
					HashMap<String, Object> hashMap3 = new HashMap();
					Iterator var23 = entrySet2.iterator();

					while(var23.hasNext()) {
						Map.Entry<String, JsonElement> m2 = (Map.Entry)var23.next();
						hashMap3.put(m2.getKey(), ((JsonElement)m2.getValue()).getAsString());
					}

					if (hashMap3.size() != 0) {
						hashMap2.put(m.getKey(), hashMap3);
					} else {
						hashMap2.put(m.getKey(), ((JsonElement)m.getValue()).getAsString());
					}

					hashMap.put("data", hashMap2);
				} else {
					if (!"SubAccount".equals(m.getKey())) {
						hashMap2.put(m.getKey(), ((JsonElement)m.getValue()).getAsString());
					} else {
						try {
							if (((JsonElement)m.getValue()).toString().trim().length() <= 2 && !((JsonElement)m.getValue()).toString().contains("[")) {
								hashMap2.put(m.getKey(), ((JsonElement)m.getValue()).getAsString());
								hashMap.put("data", hashMap2);
								break;
							}

							if (((JsonElement)m.getValue()).toString().contains("[]")) {
								hashMap2.put(m.getKey(), new JsonArray());
								hashMap.put("data", hashMap2);
								continue;
							}

							JsonArray asJsonArray = JsonParser.parseString(((JsonElement)m.getValue()).toString()).getAsJsonArray();
							ArrayList<HashMap<String, Object>> arrayList = new ArrayList();
							Iterator var20 = asJsonArray.iterator();

							while(var20.hasNext()) {
								JsonElement j = (JsonElement)var20.next();
								Set<Map.Entry<String, JsonElement>> entrySet2 = j.getAsJsonObject().entrySet();
								HashMap<String, Object> hashMap3 = new HashMap();
								Iterator var14 = entrySet2.iterator();

								while(var14.hasNext()) {
									Map.Entry<String, JsonElement> m2 = (Map.Entry)var14.next();
									hashMap3.put(m2.getKey(), ((JsonElement)m2.getValue()).getAsString());
								}

								arrayList.add(hashMap3);
							}

							hashMap2.put("SubAccount", arrayList);
						} catch (Exception var16) {
							JsonObject asJsonObject2 = JsonParser.parseString(((JsonElement)m.getValue()).toString()).getAsJsonObject();
							Set<Map.Entry<String, JsonElement>> entrySet2 = asJsonObject2.entrySet();
							HashMap<String, Object> hashMap3 = new HashMap();
							Iterator var12 = entrySet2.iterator();

							while(var12.hasNext()) {
								Map.Entry<String, JsonElement> m2 = (Map.Entry)var12.next();
								hashMap3.put(m2.getKey(), ((JsonElement)m2.getValue()).getAsString());
							}

							hashMap2.put(m.getKey(), hashMap3);
							hashMap.put("data", hashMap2);
						}
					}

					hashMap.put("data", hashMap2);
				}
			} else {
				hashMap.put(m.getKey(), ((JsonElement)m.getValue()).getAsString());
			}
		}

		return hashMap;
	}

	private HashMap<String, Object> xmlToMap(String xml) {
		HashMap<String, Object> map = new HashMap();
		Document doc = null;

		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			HashMap<String, Object> hashMap2 = new HashMap();
			Iterator i = rootElt.elementIterator();

			while(true) {
				while(true) {
					while(i.hasNext()) {
						Element e = (Element)i.next();
						if (!"statusCode".equals(e.getName()) && !"statusMsg".equals(e.getName())) {
							if (!"SubAccount".equals(e.getName()) && !"totalCount".equals(e.getName()) && !"token".equals(e.getName()) && !"downUrl".equals(e.getName())) {
								HashMap<String, Object> hashMap3 = new HashMap();
								Iterator i2 = e.elementIterator();

								while(i2.hasNext()) {
									Element e2 = (Element)i2.next();
									hashMap3.put(e2.getName(), e2.getText());
								}

								if (hashMap3.size() != 0) {
									hashMap2.put(e.getName(), hashMap3);
								} else {
									hashMap2.put(e.getName(), e.getText());
								}

								map.put("data", hashMap2);
							} else {
								if (!"SubAccount".equals(e.getName())) {
									hashMap2.put(e.getName(), e.getText());
								} else {
									ArrayList<HashMap<String, Object>> arrayList = new ArrayList();
									HashMap<String, Object> hashMap3 = new HashMap();
									Iterator i2 = e.elementIterator();

									while(i2.hasNext()) {
										Element e2 = (Element)i2.next();
										hashMap3.put(e2.getName(), e2.getText());
										arrayList.add(hashMap3);
									}

									hashMap2.put("SubAccount", arrayList);
								}

								map.put("data", hashMap2);
							}
						} else {
							map.put(e.getName(), e.getText());
						}
					}

					return map;
				}
			}
		} catch (DocumentException var12) {
			var12.printStackTrace();
			this.log.error(var12.getMessage());
		} catch (Exception var13) {
			this.log.error(var13.getMessage());
			var13.printStackTrace();
		}

		return map;
	}

	private String generateJson(String to, String templateId, String[] datas, String subAppend, String reqId) {
		JsonObject json = new JsonObject();
		json.addProperty("appId", this.App_ID);
		json.addProperty("to", to);
		json.addProperty("templateId", templateId);
		if (datas != null) {
			StringBuilder sb = new StringBuilder("[");
			String[] var8 = datas;
			int var9 = datas.length;

			for(int var10 = 0; var10 < var9; ++var10) {
				String s = var8[var10];
				sb.append("\"");
				sb.append(s);
				sb.append("\"");
				sb.append(",");
			}

			sb.replace(sb.length() - 1, sb.length(), "]");
			JsonArray jarray = JsonParser.parseString(sb.toString()).getAsJsonArray();
			json.add("datas", jarray);
		}

		if (!StringUtils.isBlank(subAppend) && ParmUtils.checkSubAppend(subAppend)) {
			json.addProperty("subAppend", subAppend);
		}

		if (!StringUtils.isBlank(reqId) && ParmUtils.checkReqId(reqId)) {
			json.addProperty("reqId", reqId);
		}

		return json.toString();
	}

	private String generateXml(String to, String templateId, String[] datas, String subAppend, String reqId) {
		StringBuilder sb = new StringBuilder("<?xml version='1.0' encoding='utf-8'?><TemplateSMS>");
		sb.append("<appId>").append(this.App_ID).append("</appId>").append("<to>").append(to).append("</to>").append("<templateId>").append(templateId).append("</templateId>");
		if (datas != null) {
			sb.append("<datas>");
			String[] var7 = datas;
			int var8 = datas.length;

			for(int var9 = 0; var9 < var8; ++var9) {
				String s = var7[var9];
				sb.append("<data>").append(s).append("</data>");
			}

			sb.append("</datas>");
		}

		if (!StringUtils.isBlank(subAppend) && ParmUtils.checkSubAppend(subAppend)) {
			sb.append("<subAppend>").append(subAppend).append("</subAppend>");
		}

		if (!StringUtils.isBlank(reqId) && ParmUtils.checkReqId(reqId)) {
			sb.append("<reqId>").append(reqId).append("</reqId>");
		}

		sb.append("</TemplateSMS>").toString();
		return sb.toString();
	}

	private StringBuffer getBaseUrl() {
		StringBuffer sb = new StringBuffer();
		if (this.USE_SSL) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}

		sb.append(this.SERVER_IP).append(":").append(this.SERVER_PORT);
		sb.append("/2013-12-26");
		return sb;
	}

	private HashMap<String, Object> getMyError(String code, String msg) {
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("statusCode", code);
		hashMap.put("statusMsg", msg);
		return hashMap;
	}

	private HashMap<String, Object> accountValidate() {
		if (StringUtils.isEmpty(this.SERVER_IP)) {
			return this.getMyError("172004", "IP为空");
		} else if (StringUtils.isEmpty(this.SERVER_PORT)) {
			return this.getMyError("172005", "端口错误");
		} else if (StringUtils.isEmpty(this.ACCOUNT_SID)) {
			return this.getMyError("172006", "主帐号为空");
		} else if (StringUtils.isEmpty(this.ACCOUNT_TOKEN)) {
			return this.getMyError("172007", "主帐号令牌为空");
		} else {
			return StringUtils.isEmpty(this.App_ID) ? this.getMyError("172012", "应用ID为空") : null;
		}
	}
}
