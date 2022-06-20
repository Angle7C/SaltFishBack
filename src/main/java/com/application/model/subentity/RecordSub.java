package com.application.model.subentity;

import com.application.model.DTO.RecordDTO;
import com.application.model.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordSub {
    private String content;
    private RecordDTO recordDTO;
}
