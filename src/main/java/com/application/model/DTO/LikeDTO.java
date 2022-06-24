package com.application.model.DTO;

import lombok.Data;

@Data
public class LikeDTO {
    private Long userId;
    private Long commentId;
    private Long reviewId;
    private Long id;
    private Boolean change;
}
