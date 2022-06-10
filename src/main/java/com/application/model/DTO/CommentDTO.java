package com.application.model.DTO;

import com.application.model.entity.Comment;
import com.application.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    @NotNull(message = "问题ID为空")
    private Long problemId;
    private Long like;
    @NotNull(message = "内容为空")
    @Length(min=100,message = "内容最小长度为100")
    private String content;
    @NotNull(message = "发送用户")
    private UserDTO user;
    @Min(value = 0,message = "状态异常")
    @Max(value = 10,message = "状态异常")
    private Integer type;
    public CommentDTO(Comment comment, User user){
        this.id=comment.getId();
        this.problemId=comment.getProblemId();
        this.like=comment.getLikess();
        this.content=comment.getContent();
        this.user=new UserDTO(user);
        this.type= comment.getType();
    }
    public Comment toEntity(){
        Comment comment = new Comment();
        comment.setUserId(this.user.getID());
        comment.setContent(this.content);
        comment.setId(this.id);
        comment.setProblemId(this.problemId);
        comment.setLikess(this.like);
        comment.setType(this.type);
        return comment;
    }

}
