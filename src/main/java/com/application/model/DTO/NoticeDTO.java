package com.application.model.DTO;

import com.application.model.entity.Notice;
import com.application.model.entity.Record;
import com.application.model.entity.Review;
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
    private ReviewDTO reviewDTO;
    private Integer type;
    public Notice toEntity(){
        return new Notice(id,reciveId,sendId,reviewDTO.getId(),type);
    }
    public NoticeDTO(Notice notice, Review review){
        this.id=notice.getId();
        this.reviewDTO=new ReviewDTO(review);
        this.sendId= notice.getSendId();
        this.reciveId= notice.getReciveId();
        this.type= notice.getType();
    }
}
