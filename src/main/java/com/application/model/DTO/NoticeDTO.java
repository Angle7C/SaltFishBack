package com.application.model.DTO;

import com.application.model.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    private Long id;
    private Long reciveId;
    private Long sendId;
    private Long emailId;
    private Integer type;
    public Notice toEntity(){
        return new Notice(id,reciveId,sendId,emailId,type);
    }
    public NoticeDTO(Notice notice){
        this.emailId=notice.getEmailId();
        this.id=notice.getId();
        this.sendId=notice.getSendId();
        this.type= notice.getType();;
        this.reciveId= notice.getId();
    }
}
