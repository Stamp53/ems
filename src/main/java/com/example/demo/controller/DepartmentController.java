package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.DepartmentRequest;
import com.example.demo.dto.DepartmentResponse;
import com.example.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse created = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Department created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request
    ) {
        DepartmentResponse updated = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Department updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Department deleted successfully"));
    }
}
