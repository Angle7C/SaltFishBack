package com.application.model.DTO;

import com.application.model.entity.Problem;
import com.application.model.entity.Tie;
import com.application.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TieDTO {
    private Long id;
    private String title;
    private UserDTO user;
    private ProblemDTO problem;
    private List<ReviewDTO> recent;
    private String desc;
    private Date time;
    public Tie toEntity(){
        Tie tie = new Tie();
        tie.setId(id);
        tie.setTitle(title);
        tie.setUserId(user.getId());
        tie.setProblemId(problem.getId());
        tie.setTime(time);
        tie.setDescs(desc);
        return tie;
    }
    public TieDTO(Tie tie){
        this.desc=tie.getDescs();
        id=tie.getId();
        title= tie.getTitle();;
        user=null;
        problem=null;
        recent=null;
        time=tie.getTime();
    }
    public TieDTO(Tie tie, User user){
        this(tie);
        this.user=new UserDTO(user);
    }
    public TieDTO(Tie tie, User user, Problem problem){
        this(tie,user);
        this.problem=new ProblemDTO(problem);
    }
//    public void addRecent(List<ReviewDTO> list){
//           this.recent=recent;
//    }
}
