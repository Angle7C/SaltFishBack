package com.application.model.subentity;



import com.application.utils.LogUtil;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.io.*;

@Data
@Component
public class CodeC {
    private String content;
    private String type;
    public File toFile(String path,Long probelmID,Long UserId) throws FileNotFoundException {
        File file=new File(path+File.separator+probelmID.toString());
        if(!file.exists()) {
            file.mkdirs();
        }
            file=new File(file.getAbsoluteFile()+File.separator+UserId.toString()+"_source.c");
        try {
                file.createNewFile();
        } catch (IOException e) {
                LogUtil.info("创建文件失败");
                return null;
        }
        try(OutputStream outputStream=new FileOutputStream(file);
            OutputStreamWriter writer=new OutputStreamWriter(outputStream);){
            writer.write(content);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.info("文件写入错误:{}",e);
        }
        LogUtil.info("文件写入成功");
        return file;
    }

}
