package com.javaweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prize {

    private Integer id;
    private String name;
    private Integer cost;
    private String imgSrc;
}
