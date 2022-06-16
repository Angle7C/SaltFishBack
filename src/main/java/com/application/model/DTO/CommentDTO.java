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
    private Long problemId;
    private Long like;
    private String content;
    private UserDTO user;
    private Integer type;
    public CommentDTO(Comment comment, User user){
        this.id=comment.getId();
        this.problemId=comment.getProblemId();
        this.like=comment.getLikess();
        this.content=comment.getContext();
        this.user=new UserDTO(user);
        this.type= comment.getType();
    }
    public Comment toEntity(){
        Comment comment = new Comment();
        comment.setUserId(this.user.getId());
        comment.setContext(this.content);
        comment.setId(this.id);
        comment.setProblemId(this.problemId);
        comment.setLikess(this.like);
        comment.setType(this.type);
        return comment;
    }

}
