package com.javaweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer id;
    private Integer userId;
    private Integer prizeId;
    private Integer quantity;
    private String name;
    private Integer cost;
    private String imgSrc;
}
