package com.codecademy.imagestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
