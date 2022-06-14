package com.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogUtil {
    private static final Logger log= LoggerFactory.getLogger("日志");
    public  static void debug(String formt,Object...pram){
        log.debug(formt,pram);
    }
    public  static void error(String formt,Object...pram){
        log.error(formt,pram);
    }
    public  static void info(String formt,Object...pram){
        log.info(formt,pram);
    }

}
