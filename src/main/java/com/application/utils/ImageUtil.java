package com.application.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
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
}
