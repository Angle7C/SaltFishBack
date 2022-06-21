package enums;

import cn.hutool.crypto.digest.MD5;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.message.Message;
@Getter
public enum UserToken {
    Admin_TOKEN(MD5.create().digestHex("Admin","UTF-8"));

    private String message;
    UserToken(String message){
        this.message=message;
    }
}
