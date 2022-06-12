package com.application.utils;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static final List<String> list=new ArrayList<>();
    static {
        list.add("png");
        list.add("jpg");
        list.add("jpeg");
    }
    public static String checkSuffix(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        LogUtil.info("获取文件后缀:{}",suffix);
        if(!list.contains(suffix)){
            return null;
        }
        else
            return "."+suffix;
    }
}
