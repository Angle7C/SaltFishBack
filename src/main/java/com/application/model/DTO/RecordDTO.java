package com.application.model.DTO;

import com.application.model.entity.Problem;
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
    private Long score=0L;
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
    public RecordDTO(Record record){
        this.user=null;
        this.problem=null;
        this.score=record.getScore();
        this.id=record.getId();
        this.path=record.getPath();
        this.type=record.getType();
    }
    public RecordDTO(Record record, Problem problem){
        this.user=null;
        this.problem=new ProblemDTO(problem);
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
    public RecordDTO HiddePath(){
        this.path=null;
        return this;
    }
    public void addSocre(boolean judge){
        score <<= 1;
        score |= judge?1:0;
    }
}
