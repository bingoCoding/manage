package com.bingo.manage.modular.system.controller;

import com.github.pagehelper.PageHelper;
import com.bingo.manage.common.annotion.Permission;
import com.bingo.manage.common.annotion.log.BussinessLog;
import com.bingo.manage.common.constant.Const;
import com.bingo.manage.common.controller.BaseController;
import com.bingo.manage.common.page.PageReq;
import com.bingo.manage.common.persistence.dao.LoginLogMapper;
import com.bingo.manage.common.persistence.model.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 日志管理的控制器
 *
 * @author bingo
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    private static String PREFIX = "/system/log/";

    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "login_log.html";
    }

    /**
     * 查询登录日志列表
     */
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName) {
        PageReq params = defaultPage();
        PageHelper.offsetPage(params.getOffset(), params.getLimit());
        List<Map<String, Object>> result = loginLogMapper.getLoginLogs(beginTime, endTime, logName, params.getSort(), params.isAsc());
        return packForBT(result);
    }

    /**
     * 清空日志
     */
    @BussinessLog("清空登录日志")
    @RequestMapping("/delLoginLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        loginLogMapper.delete(new LoginLog());
        return super.SUCCESS_TIP;
    }
}
