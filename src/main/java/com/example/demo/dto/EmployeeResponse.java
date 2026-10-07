package com.example.demo.dto;

import com.example.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private BigDecimal salary;
    private Role role;
    private Long departmentId;
    private String departmentName;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
