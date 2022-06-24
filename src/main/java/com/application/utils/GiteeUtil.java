package com.application.utils;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.ParameterizedTypeImpl;
import cn.hutool.http.HttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class GiteeUtil {
    private static  final String access_token="3ec80afd039b170274eb820e0c6efa97";
    private static  final String owner="mousetrap/image";
    private static  final String URL="https://gitee.com/api/v5/repos/"+owner+"/contents/avatar/";
    private static  final JsonParser jsonParser=new JsonParser();
    public static String  upload(MultipartFile file,String path,Long userId){
           String imagePath=path+ File.separator+userId.toString()+File.separator+createUploadURL(file.getOriginalFilename());
           File image=new File(imagePath);
           if(image.getParentFile().mkdirs()){
               image.getParentFile().mkdirs();
           }
        try {
            file.transferTo(image);
            return imagePath;
        } catch (IOException e) {
            return null;
        }
    }
    public static String  upload(MultipartFile[] files,String path,Long problemId){
        String imagePath=null;
        int i=0;
        for (MultipartFile file : files) {
            imagePath=path+ File.separator+problemId.toString()+File.separator+file.getOriginalFilename();
            try {
                file.transferTo(new File(imagePath));
            } catch (IOException e) {

            }
        }
        return path+File.separator+problemId.toString()+File.separator;
    }
    public static String  upload(MultipartFile file)  {
        String uploadURL = createUploadURL(file.getOriginalFilename());

        Map<String, Object> uploadBody = null;
        try {
            uploadBody = createUploadBody(file.getBytes());
        } catch (IOException e) {
            LogUtil.error("文件上传失败, 异常信息：{}",e.getMessage());
        }
        Assert.notNull(uploadBody,"文件传输异常");
        String json = HttpUtil.post(uploadURL, uploadBody);
        return getAvatarUrl(json);

    }
    private static String getAvatarUrl(String json){
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        JsonElement content = jsonObject.get("content");
        return content.getAsJsonObject().get("download_url").getAsString();

    }
    public static String createUploadURL(String fileName){
        String suffix = FileUtil.checkSuffix(fileName);
        Assert.notNull(suffix,"不支持这个文件后缀,支持");
        return UUID.randomUUID().toString()+suffix;
    }
    public static Map<String,Object> createUploadBody(byte[] data){
        Map<String,Object> map=new HashMap<>();
        String base64= Base64.encode(data);
        map.put("access_token",access_token);
        map.put("content",base64);
        map.put("message","头像图片");
        return map;
    }
    public static Map<String,Object> createUploadBody(String base64){
        Map<String,Object> map=new HashMap<>();
        map.put("access_token",access_token);
        map.put("content",base64);
        map.put("message","头像图片");
        return map;
    }

}