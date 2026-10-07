package com.example.demo.service;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);
}
