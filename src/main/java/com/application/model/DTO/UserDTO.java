package com.application.model.DTO;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.MD5;
import com.application.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String passWord;
    private String avatarUrl;
    private Long ranks;
    private Long id;
    private String description;
    public User toEntity(){
        User user = new User();
        user.setRanks(ranks);
        user.setUserName(name);
        user.setImageUrl(avatarUrl);
        user.setPassWord(MD5.create().digestHex(passWord,"UTF-8"));
        user.setId(id);
        user.setDecription(description);
        return user;
    }
    public UserDTO(User user){
        Assert.notNull(user,"没有这个用户");
        this.name=user.getUserName();
        this.passWord=null;
        this.avatarUrl=user.getImageUrl();
        this.ranks=user.getRanks();
        this.id=user.getId();
        this.description=user.getDecription();
    }
}
