package com.application.controller;

import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.service.ProblemService;
import com.application.service.RecordService;
import com.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminRecordController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private RecordService recordService;
    @GetMapping("/record/{id}")
    public ResultJson getRecordUser(@PathVariable("id") Long id){
        List<RecordDTO> list = recordService.selectUserId(id);
        return new ResultJson().ok("");
    }
}
