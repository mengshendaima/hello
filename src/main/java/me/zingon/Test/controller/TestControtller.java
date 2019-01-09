package me.zingon.Test.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.zingon.Test.service.TestService;

@RestController
@RequestMapping("/taskTest")
public class TestControtller {

	@Autowired
	TestService testService;

	@RequestMapping("/list.do")
//	@ResponseBody
	public Object listAll(HttpServletRequest request) {
		try {
			String token = request.getHeader("token");
			if (token != null) {
				return testService.listAll();
			}
			return "谢谢，部署成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "对不起";
		}

	}

	@RequestMapping("/page.@-@")
	public String resolveTo() {
		return "/test";
	}
	@RequestMapping("/do")
	public String taskTest() {
		
		return "/test";
	}
	@RequestMapping("/cookieTest")
	public void addCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("name_test","value_test");//创建新cookie
        cookie.setMaxAge(5 * 60);// 设置存在时间为5分钟
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端
    }
}
