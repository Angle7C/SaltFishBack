package com.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class WxStateUtil {
    private static final Logger logger= LoggerFactory.getLogger("微信记录器");
    private static final Map<String,Long> stateMap=new ConcurrentHashMap<>();
    private static final Map<String, Long> timeMap=new ConcurrentHashMap<>();
    public  static void  addState(String state,Long time,Long value){
        logger.info("添加state:{} 时间：{}",state,time);
        stateMap.put(state,value);
        timeMap.put(state,new Date().getTime()+time);
        logger.info("结束");

    }
    public  static void   changeSate(String state,Long value,Long time){
        logger.info("更新state:{} 时间：{}，值为 {}",state,time,value);
        stateMap.replace(state,value);
        timeMap.replace(state,new Date().getTime()+time);
        logger.info("结束");
    }
    public static void remove(String state){
        logger.info("删除state:{}",state);
        stateMap.remove(state);
        timeMap.remove(stateMap);
        logger.info("结束");
    }
    public static Long checkState(String state){
        Long value=null;
        try{
             value=stateMap.get(state);
        }catch (Exception e){
            value=null;
        }
        return value;
    }
    @Scheduled(cron = "0/30 * * * * ? ")
    public static synchronized void timeRmove(){
        logger.info("定时清除state,清除时间:{}",new Date());

        Long now=new Date().getTime();
        List<String> list=new LinkedList<>();
        timeMap.forEach((state,time)->{
            if(time<now){
                list.add(state);
            }
        });
        Iterator<Map.Entry<String, Long>> iterator = timeMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Long> next = iterator.next();
            if(next.getValue()<now){
                logger.info("删除state {}",next.getKey());
                iterator.remove();
            }
        }
        logger.info("清除结束，清除个数: {}",list.size());
    }
}
