package me.zingon.Test.service.impl;

import org.springframework.stereotype.Service;

@Service
public class TaskJob {
/*	秒    0-59    , - * / 
	分    0-59    , - * / 
	小时    0-23    , - * / 
	日期    1-31    , - * ? / L W C 
	月份    1-12 或者 JAN-DEC    , - * / 
	星期    1-7 或者 SUN-SAT    , - * ? / L C # 
	年（可选）    留空, 1970-2099    , - * /  
	- 区间 
	* 通配符 
	? 你不想设置那个字段 */
	public void job1(){
		System.out.println("定时任务正在进行~~~~~");
	}
}
