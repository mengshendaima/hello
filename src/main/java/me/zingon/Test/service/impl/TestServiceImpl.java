package me.zingon.Test.service.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zingon.Test.dao.TestDao;
import me.zingon.Test.model.Test;
import me.zingon.Test.service.TestService;

/**
 * Created by ZTC on 2/21/2017.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestDao testDao;

    @Override
    public List<Test> listAll() {
        return testDao.listAll();
    }
    public void test2(){
    	String time = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("test2定时器触发打印" + time);
    }
}
