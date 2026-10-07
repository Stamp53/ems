package com.example.demo.service;

import com.example.demo.dto.EmployeeResponse;
import com.example.demo.dto.EmployeeUpdateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeResponse> getAllEmployees(Long departmentId, Boolean active, Pageable pageable);

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse getEmployeeByEmail(String email);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void deleteEmployee(Long id);
}
