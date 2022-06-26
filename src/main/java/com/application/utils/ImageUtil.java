package com.application.utils;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageUtil {
    private static QrConfig initQrConfig() {
        QrConfig config = new QrConfig();
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(new Color(60,189,60));
        // 设置背景色（灰色）
        config.setBackColor(Color.WHITE);
        return config;
    }

    public static void createQRCode2Stream(String content, HttpServletResponse response,String appid,String nap,String state) {
        String QrUrl = String.format(content, appid, nap+"/wxlogin", state);
        try {
            QrCodeUtil.generate(QrUrl, initQrConfig(), "png", response.getOutputStream());
            log.info("生成二维码成功!");
        } catch (QrCodeException | IOException e) {
            log.error("发生错误！ {}！", e.getMessage());
        }
    }
    public static String getimagestr(String url)

    {//将图片文件转化为字节数组字符串，并对其进行base64编码处理

        String imgfile = url;
        InputStream in = null;
        byte[] data = null;
//读取图片字节数组
        try
        {
            in = new FileInputStream(imgfile);

            data = new byte[in.available()];

            in.read(data);

            in.close();

        }
        catch (IOException e)

        {
            throw new IllegalArgumentException("没有这个文件");

        }
//对字节数组base64编码
        Base64Encoder encoder = new Base64Encoder();
        return encoder.encode(data);//返回base64编码过的字节数组字符串

    }
    public static String urlToImage(String url,String down){
        HttpResponse execute = HttpUtil.createGet(url)
                .timeout(-1).execute();
        execute.writeBody(down);
//        HttpUtil.downloadFile(url, down);

        return  down;
    }

}