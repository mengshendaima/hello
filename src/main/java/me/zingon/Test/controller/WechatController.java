package me.zingon.Test.controller;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import me.zingon.Test.model.AccessToken;
import me.zingon.Test.model.Filter;
import me.zingon.Test.model.Text;
import me.zingon.Test.model.ToPostWechat;

@RequestMapping("/wechat")

@Controller

public class WechatController {
//	private static String WECHAT_TOKEN = "jinsanpang";写个token不知道干啥的，很烦
	@RequestMapping(value = "/wx.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestTemplate restTemplate=new RestTemplate();
//		restTemplate = new RestTemplate();

		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
		converterList.remove(1);    //移除StringHttpMessageConverter
		HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		converterList.add(1, converter);    //convert顺序错误会导致失败

		restTemplate.setMessageConverters(converterList);
		
		
		System.out.println("========WechatController========= ");
		Enumeration pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String name = (String) pNames.nextElement();
			String value = request.getParameter(name);
			String log = "name =" + name + "     value =" + value;
		}
		String signature = request.getParameter("signature");/// 微信加密签名
		String timestamp = request.getParameter("timestamp");/// 时间戳
		String nonce = request.getParameter("nonce"); /// 随机数
		String echostr = request.getParameter("echostr"); // 随机字符
		PrintWriter out = response.getWriter();
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
		String access_token=getAccess_token("wxb82a496071ea493d","909ef1ecffa2f63a30009d833c6c53d5");
		System.out.println(access_token);
		String url="https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+access_token;
		
		//构造请求参数
		ToPostWechat jsonString=new ToPostWechat();
		jsonString.setMsgtype("text");
		Filter filter=new Filter();
		filter.setIs_to_all(false);
		filter.setTag_id(2);
		Text text=new Text();
		text.setContent("haha啊啊");
		jsonString.setFilter(filter);
		jsonString.setText(text);
		//构造结束
		
		String ss=new Gson().toJson(jsonString);
		System.out.println(ss);//https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,jsonString, String.class);
		
		String tags = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/tags/get?access_token="+access_token, String.class);
//		System.out.println(tags.get(1).toString());
		System.out.println("得到的标签是"+tags);
//		return responseEntity.getBody();
	}
	public static String getAccess_token(String appid, String appsecret) {
		RestTemplate restTemplate=new RestTemplate();
		// 获取access_token
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" + "&appid=" + appid
				+ "&secret=" + appsecret;
		String json = restTemplate.getForObject(url, String.class);
		AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
		return accessToken.getAccess_token();
	}
}
