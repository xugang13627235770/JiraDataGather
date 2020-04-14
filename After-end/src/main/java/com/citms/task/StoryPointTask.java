//package com.citms.task;
//
//import com.citms.modules.jira.service.StoryPointRecordService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//
///**
// * 类StoryPointTask的实现描述：TODO 类实现描述
// * @author DELL 2019/11/30 10:27
// */
//@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//public class StoryPointTask {
//
//    @Autowired
//    private StoryPointRecordService storyPointRecordService;
//
//
//    //3.添加定时任务，每天23:50:00执行0 50 23 * * ?
//    @Scheduled(cron = "0 50 23 * * ?")
//    //或直接指定时间间隔，例如：5秒
//    //@Scheduled(fixedRate=5000)
//    private void storyPointTasks() {
//        try {
//            storyPointRecordService.saveStoryPointRecord();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//}