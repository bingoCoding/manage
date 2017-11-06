package com.bingo.manage.modular.biz.service.impl;

import com.bingo.manage.common.annotion.DataSource;
import com.bingo.manage.common.constant.DSEnum;
import com.bingo.manage.common.persistence.dao.TestMapper;
import com.bingo.manage.common.persistence.model.Test;
import com.bingo.manage.modular.biz.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试服务
 *
 * @author bingo
 * @date 2017-06-23 23:02
 */
@Service
public class TestServiceImpl implements ITestService {


    @Autowired
    TestMapper testMapper;

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void testBiz() {
        Test test = testMapper.selectByPrimaryKey(1);
        test.setId(22);
        testMapper.insert(test);
    }


    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_manage)
    public void testmanage() {
        Test test = testMapper.selectByPrimaryKey(1);
        test.setId(33);
        testMapper.insert(test);
    }

    @Override
    @Transactional
    public void testAll() {
        testBiz();
        testmanage();
        //int i = 1 / 0;
    }

}
