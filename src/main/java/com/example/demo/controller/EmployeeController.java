package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.dto.EmployeeUpdateRequest;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getAllEmployees(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        Page<EmployeeResponse> employees = employeeService.getAllEmployees(departmentId, active, pageable);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        EmployeeResponse employee = employeeService.getEmployeeByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        EmployeeResponse updated = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Employee updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Employee deactivated successfully"));
    }
}
