package com.application.model.DTO;

import com.application.model.entity.Review;
import com.application.model.entity.Tie;
import com.application.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.parsing.Problem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private Long likes;
    private TieDTO tie;
    public Review toEntity(){
        Review review = new Review();
        review.setId(id);
        review.setContent(content);
        review.setLikes(likes);
        review.setUserId(user.getId());
        review.setProblemId(tie.getId());
        return  review;
    }
    public ReviewDTO(Review review){
        this.id=review.getId();
        this.user=null;
        this.content=review.getContent();
        this.likes= review.getLikes();
        this.tie=null;
    }
    public ReviewDTO(Review review, User user){
        this(review);
        this.user=new UserDTO(user);
    }
    public ReviewDTO(Review review, User user, Tie tie){
        this(review,user);
        this.tie=new TieDTO(tie);
    }

}
