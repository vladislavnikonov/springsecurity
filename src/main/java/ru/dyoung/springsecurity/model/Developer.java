package ru.dyoung.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Developer {
    private Long id;
    private String userName;
    private String lastName;
}
