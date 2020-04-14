/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.citms.modules.jira.controller;

import com.citms.common.utils.R;
import com.citms.modules.jira.service.JiraDataService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * APP测试接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/")
@Api("APP测试接口")
@Slf4j
public class JiraDataController {

    @Autowired
    private JiraDataService jiraDataService;

    @RequestMapping("jiraDataReport")
    public R jiraDataReport(HttpServletRequest request, HttpServletResponse response){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        if(sb == null){
            return R.error();
        }

        try {
            jiraDataService.saveJiraData(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }

}
