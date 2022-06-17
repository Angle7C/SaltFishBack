package com.application.model.DTO;

import com.application.model.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {
    private Long id;
    private UserDTO user;
    private ProblemDTO problem;
    private Long score;
    private String path;
    private Integer type;
    public RecordDTO(Record record,UserDTO userDTO,ProblemDTO problem){
        this.user=userDTO;
        this.problem=problem;
        this.score=record.getScore();
        this.id=record.getId();
        this.path=record.getPath();
        this.type=record.getType();
    }
    public Record toEntity(){
        Record record = new Record();
        record.setId(id);
        record.setScore(score);
        record.setPath(path);
        record.setType(type);
        record.setGmtCreate(null);
        record.setGmtModified(null);
        record.setUserId(user.getId());
        record.setProblemId(problem.getId());
        return record;
    }
    public void addSocre(int num){
        if(score==null){
            score=(1L<<num);
        }else{
            score=score|(1L<<num);
        }
    }
}
