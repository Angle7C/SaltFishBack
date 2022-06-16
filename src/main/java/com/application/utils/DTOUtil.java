package com.application.utils;

import com.application.model.DTO.ProblemDTO;
import com.application.model.DTO.RecordDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.Problem;
import com.application.model.entity.Record;
import com.application.model.entity.User;

public class DTOUtil {
    public static Record getNewRecord(User user, Problem problem, String path){
        Record record=new Record();
        record.setPath(path);
        record.setUserId(user.getId());
        record.setProblemId(problem.getId());
        record.setGmtCreate(System.currentTimeMillis());
        record.setGmtModified(record.getGmtCreate());
        record.setType(0);
        record.setScore(0L);
        return record;
    }
}
