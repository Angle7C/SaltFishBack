package com.application.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.application.model.entity.Sign;
import com.application.model.subentity.SignDesc;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUtil {
    private static final String[] lucks=new String[]{"运气爆炸","平平无奇","不要出门"};
    private static final Map<Integer,SignDesc> yesMap=new HashMap<>();
    private static final Map<Integer,SignDesc> noMap=new HashMap<>();

    static {
        yesMap.put(1,new SignDesc("吃薯条","薯条刚炸出来的酥酥脆脆"));
        yesMap.put(2,new SignDesc("学习珂学","珂朵莉睡着了"));
        yesMap.put(3,new SignDesc("看书","今天看书很在状态，适合看志贺直哉的《暗夜行路》"));
        yesMap.put(4,new SignDesc("出勤","机厅就像是被你和你的小朋友承包了"));
        yesMap.put(5,new SignDesc("拜大佬","何大佬跟你讨论怎么做题了"));
        yesMap.put(6,new SignDesc("水群","明日龙王就是你"));

        noMap.put(1,new SignDesc("吃柠檬","已经酸死了"));
        noMap.put(2,new SignDesc("水群","龙王大水淹了群"));
        noMap.put(3,new SignDesc("寄","真寄了"));
        noMap.put(4,new SignDesc("唠嗑","被拉黑了"));
        noMap.put(5,new SignDesc("复习","你复习到的老师刚好不想考。"));
        noMap.put(6,new SignDesc("运动","刚出门就踩到狗屎。"));


    }
    public  static String yesDesc(){
        JSONArray jsonArray=new JSONArray();
        int i = RandomUtil.randomInt(1,7);
        jsonArray.put(yesMap.get(i));
        jsonArray.put(yesMap.get(RandomUtil.randomInt(i)+1));
        return jsonArray.toString();
    }
    public static String noDesc(){
        JSONArray jsonArray=new JSONArray();
        int i = RandomUtil.randomInt(1,7);
        jsonArray.put(noMap.get(i));
        jsonArray.put(noMap.get(RandomUtil.randomInt(i)+1));
        return jsonArray.toString();
    }
    public static Sign getSign(Long i, Date data){
        return new Sign(i,yesDesc(),noDesc(),data,lucks[i.intValue()]);

    }
}
