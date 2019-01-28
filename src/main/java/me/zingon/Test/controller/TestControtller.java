package me.zingon.Test.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

import me.zingon.Test.dao.KBPReturned;
import me.zingon.Test.framework.AesException;
import me.zingon.Test.framework.ImageMessage;
import me.zingon.Test.framework.InputMessage;
import me.zingon.Test.framework.MsgType;
import me.zingon.Test.framework.OutputMessage;
import me.zingon.Test.framework.SerializeXmlUtil;
import me.zingon.Test.framework.WXBizMsgCrypt;
import me.zingon.Test.framework.WXConstants;
import me.zingon.Test.framework.XMLParse;
import me.zingon.Test.service.TestService;

@RestController
@RequestMapping("/taskTest")
public class TestControtller {
	@Autowired
	TestService testService;

	String sToken = "worinimaye";// 这个Token是随机生成，但是必须跟企业号上的相同
	String sCorpID = "wwdd7c8102f55a0527";// 这里是你企业号的CorpID
	String sEncodingAESKey = "Pjygagdpeq3jSz3Eg0UrdQJppPpfkyrd5iOT7I3s7NU";// 这个EncodingAESKey是随机生成，但是必须跟企业号上的相同
	String accesstoken=null;
	@RequestMapping("/list.do")
	// @ResponseBody
	public Object listAll(HttpServletRequest request) {
		try {
			String token = request.getHeader("token");
			if (token != null) {
				return testService.listAll();
			}
			return "谢谢，部署第四次";
		} catch (Exception e) {
			e.printStackTrace();
			return "对不起";
		}

	}

	@RequestMapping("/cookieTest")
	public void addCookie(HttpServletResponse response, HttpServletRequest request1) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		HttpSession session = request.getSession();// 创建session对象
		String sessionId = request1.getHeader("idpSessionId");
		Cookie[] cookieList = request.getCookies();
		String sessionId1 = null;
		for (Cookie cookie : cookieList) {
			System.out.println(cookie.getName());
			if (true) {
				sessionId1 = sessionId;
			}
		}

		System.out.println(sessionId1);
		System.out.println(session.getId());
		JSONObject userInfo = new JSONObject();
		userInfo.put("name", "limeng");
		userInfo.put("password", 1234556);
		session.setAttribute("u" + "" + "ser", userInfo);
		System.out.println(request.getSession().getAttribute("user"));

		Cookie cookie = new Cookie("idpSessionId", session.getId());// 创建新cookie
		cookie.setMaxAge(60);// 设置存活时间
		cookie.setPath("/s");// 设置作用域
		response.addCookie(cookie);// 将cookie添加到response的cookie数组中返回给客户端
	}

	// 验证企业微信URL
	// @RequestMapping("/wxCompanyTest")
	// public void wxCompanyTest(HttpServletRequest request,HttpServletResponse
	// response) throws IOException, AesException{
	//
	// String sToken = "worinimaye";//这个Token是随机生成，但是必须跟企业号上的相同
	// String sCorpID = "wwdd7c8102f55a0527";//这里是你企业号的CorpID
	// String sEncodingAESKey =
	// "Pjygagdpeq3jSz3Eg0UrdQJppPpfkyrd5iOT7I3s7NU";//这个EncodingAESKey是随机生成，但是必须跟企业号上的相同
	//
	// // 微信加密签名
	// String sVerifyMsgSig = request.getParameter("msg_signature");
	// // 时间戳
	// String sVerifyTimeStamp = request.getParameter("timestamp");
	// // 随机数
	// String sVerifyNonce = request.getParameter("nonce");
	// // 随机字符串
	// String sVerifyEchoStr = request.getParameter("echostr");
	// String sEchoStr = null; //需要返回的明文
	// WXBizMsgCrypt wxcpt = null;
	// PrintWriter out = response.getWriter();
	//
	// wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
	//
	// sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,sVerifyNonce,
	// sVerifyEchoStr);
	//
	// System.out.println(sEchoStr);
	// out.print(sEchoStr);
	// return sEchoStr;

	// 验证URL成功，将sEchoStr返回
	// }

	@RequestMapping("/wxCompanyTest")
	public void wxCompanyTest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, AesException {
		WXBizMsgCrypt wxBizMsgCrypt= new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		// 微信加密签名
		String sVerifyMsgSig = request.getParameter("msg_signature");
		// 时间戳
		String sVerifyTimeStamp = request.getParameter("timestamp");
		// 随机数
		String sVerifyNonce = request.getParameter("nonce");
		// 随机字符串
		// String sVerifyEchoStr = request.getParameter("echostr");
		String result = getDecryptMsg(request, sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce);
		RestTemplate restTemplate=new RestTemplate();
//		System.out.println(result);
		Map<String,String> param=acceptMessage(response,result);
		String sendAppMsgPreUrl=WXConstants.SEND_APP_MSG_PREURL;
		String accessToken=WXConstants.ACCESS_TOKEN;
		String kbpAskUrl=WXConstants.KBP_ASK_URL;
		String content=param.get("content");
		System.out.println(content);
		String customerName=param.get("custermname");
		int agentId=WXConstants.AGENTID;
		JSONObject body=new JSONObject();
		JSONObject text=new JSONObject();
		JSONObject req=new JSONObject();
		req.put("appId", "NL7P32Wz");
		req.put("question", content);
		req.put("userId", "123");
		req.put("platform", "android");
		req.put("roleId", "1");
		ResponseEntity<String> returned=restTemplate.postForEntity(kbpAskUrl, req, String.class);
		String bodyReturn=returned.getBody();
		KBPReturned kbpReturned= JSONObject.parseObject(bodyReturn, KBPReturned.class);
		System.out.println(kbpReturned.getMsg());
		String answer;
		if(kbpReturned.getCode()==0)
			try{
		 answer=kbpReturned.getData().getList().get(0).getAnswer();
		 
			}catch(Exception e){
				System.out.println(e);
				answer="这是什么意思呀";
			}
		else
			answer="啊哦，再问我点什么吧";
		System.out.println(answer);
		text.put("content", answer);
		body.put("touser", customerName);
		body.put("msgtype", "text");
		body.put("agentid", agentId);
		body.put("text", text);
		body.put("safe", 0);
		
		restTemplate.postForEntity(sendAppMsgPreUrl+accessToken, body, String.class);
//		String resultSend=wxBizMsgCrypt.EncryptMsg(messageSend,sVerifyTimeStamp,sVerifyNonce);
//		PrintWriter out = response.getWriter();
//		out.print(resultSend);
	}

	public String getDecryptMsg(HttpServletRequest request, String msg_signature, String timestamp, String nonce) {

		String postData = ""; // 密文，对应POST请求的数据
		String result = ""; // 明文，解密之后的结果

		try {
			// 1.获取加密的请求消息：使用输入流获得加密请求消息postData
			ServletInputStream in = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String tempStr = ""; // 作为输出字符串的临时串，用于判断是否读取完毕
			while (null != (tempStr = reader.readLine())) {
				postData += tempStr;
			}

			// 2.获取消息明文：对加密的请求消息进行解密获得明文
			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
			result = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (AesException e) {
			e.printStackTrace();
		}

		return result;
	}

	private Map<String,String> acceptMessage( HttpServletResponse response,String result) throws IOException {
		Map<String,String> param=new HashMap<String,String>();
		// 处理接收消息

//		ServletInputStream in = request.getInputStream();

		// 将POST流转换为XStream对象

		XStream xs = SerializeXmlUtil.createXstream();

		xs.processAnnotations(InputMessage.class);

		xs.processAnnotations(OutputMessage.class);

		// 将指定节点下的xml节点数据映射为对象

		xs.alias("xml", InputMessage.class);

		// 将流转换为字符串

		StringBuilder xmlMsg = new StringBuilder();

//		byte[] b = new byte[4096];
//
//		for (int n; (n = in.read(b)) != -1;) {
//
//			xmlMsg.append(new String(b, 0, n, "UTF-8"));
//
//		}

		// 将xml内容转换为InputMessage对象

		InputMessage inputMsg = (InputMessage) xs.fromXML(result);

		String servername = inputMsg.getToUserName();// 服务端

		String custermname = inputMsg.getFromUserName();// 客户端

		long createTime = inputMsg.getCreateTime();// 接收时间
		
		long agentId=inputMsg.getAgentID();//agentId

		Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间
		
		String content=inputMsg.getContent();
		// 取得消息类型

		String msgType = inputMsg.getMsgType();
		StringBuffer str = new StringBuffer();
		// 根据消息类型获取对应的消息内容

		if (msgType.equals(MsgType.Text.toString())) {

			/**
			 * 返回文本消息；
			 */

			System.out.println("开发者微信号：" + inputMsg.getToUserName());

			System.out.println("发送方帐号：" + inputMsg.getFromUserName());

			System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));

			System.out.println("消息内容：" + inputMsg.getContent());

			System.out.println("消息Id：" + inputMsg.getMsgId());

			

			str.append("<xml>");

			str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");

			str.append("<FromUserName><![CDATA[" + servername + "]]></FromUEserName>");

			str.append("<CreateTime>" + returnTime + "</CreateTime>");

			str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");

			str.append("<Content><![CDATA[你说的是：" + content + "，吗？]]></Content>");

			str.append("</xml>");

			System.out.println(str.toString());
			
			param.put("custermname", custermname);
			param.put("content", content);
//			response.getWriter().write(str.toString());
			return param;

		}

		/**
		 * 返回图片消息；
		 */

		if (msgType.equals(MsgType.Image.toString())) {

			System.out.println("获取多媒体信息");

			System.out.println("多媒体文件id：" + inputMsg.getMediaId());

			System.out.println("图片链接：" + inputMsg.getPicUrl());

			System.out.println("消息id，64位整型：" + inputMsg.getMsgId());

			OutputMessage outputMsg = new OutputMessage();

			outputMsg.setFromUserName(servername);

			outputMsg.setToUserName(custermname);

			outputMsg.setCreateTime(returnTime);

			outputMsg.setMsgType(msgType);

			ImageMessage images = new ImageMessage();

			images.setMediaId(inputMsg.getMediaId());

			outputMsg.setImage(images);

			System.out.println("xml转换：/n" + xs.toXML(outputMsg));

//			response.getWriter().write(xs.toXML(outputMsg));
			return param;

		}
		
		return param;

	}
	

}