package com.javaweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private LocalDate birthDate;
    private Integer recordSeconds;
    private Integer bonus;
    private Integer role;
    private String salt;
}
