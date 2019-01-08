package me.zingon.Test.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import me.zingon.Test.model.AccessToken;
import me.zingon.Test.model.TemplateData;
import me.zingon.Test.model.WxMssVo;

@RestController
public class WechatTestPushController {/*
	// 用来请求微信的get和post
	
	private RestTemplate restTemplate;

	*//**
	 * 微信小程序推送单个用户
	 *//*
	@RequestMapping("/wechat11")
	public String pushOneUser(@Param("openid")String openid,@Param("formid") String formid) {//@RequestBody Map<String,Object>param

		// 获取access_token
		String access_token = getAccess_token("wxc066ef72932643d9", "664b9be3d3a226380a772397c8e7a32c");
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + "?access_token=" + access_token;

		// 拼接推送的模版
//		String openid="216513135";
//		String formid="2315135135";
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(openid);// 用户openid
		wxMssVo.setTemplate_id("Uwt-c_b-Ipalqco_iZ6UcBhrv3gQWUGhnxkcZFSsnME");// 模版id
		wxMssVo.setForm_id(formid);// formid

		Map<String, TemplateData> m = new HashMap<>(5);

		// keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
		TemplateData value1 = new TemplateData();
		value1.setValue1("姓名");
		m.put("keyword1", value1);

		TemplateData value2 = new TemplateData();
		value2.setValue2("书籍");
		m.put("keyword2", value2);
		wxMssVo.setData(m);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
		return responseEntity.getBody();
	}

	*//**
	 * 获取access_token appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
	 *//*
	public String getAccess_token(String appid, String appsecret) {
		// 获取access_token
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" + "&appid=" + appid
				+ "&secret=" + appsecret;
		String json = restTemplate.getForObject(url, String.class);
		AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
		return accessToken.getAccess_token();
	}
*/}
