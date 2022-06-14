package com.application.model.subentity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page{
    private  Long totalPage;
    private  Integer pageSize;
    private  Integer pageIndex;

}
