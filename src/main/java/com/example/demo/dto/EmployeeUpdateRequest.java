package com.example.demo.dto;

import com.example.demo.model.Role;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeUpdateRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private BigDecimal salary;
    private Role role;
    private Long departmentId;
    private Boolean active;
}
