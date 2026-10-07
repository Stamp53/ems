package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.LeaveRequestCreateDto;
import com.example.demo.dto.LeaveRequestResponse;
import com.example.demo.dto.LeaveStatusUpdateRequest;
import com.example.demo.model.LeaveStatus;
import com.example.demo.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> applyLeave(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LeaveRequestCreateDto request
    ) {
        LeaveRequestResponse response = leaveService.applyLeave(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Leave request submitted successfully"));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> getMyLeaves(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<LeaveRequestResponse> leaves = leaveService.getMyLeaves(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(ApiResponse.success(leaves));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> getAllLeaves(
            @RequestParam(required = false) LeaveStatus status,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<LeaveRequestResponse> leaves = leaveService.getAllLeaves(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(leaves));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> getDepartmentLeaves(
            @PathVariable Long departmentId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<LeaveRequestResponse> leaves = leaveService.getDepartmentLeaves(departmentId, pageable);
        return ResponseEntity.ok(ApiResponse.success(leaves));
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> reviewLeave(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LeaveStatusUpdateRequest request
    ) {
        LeaveRequestResponse response = leaveService.reviewLeave(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Leave request review submitted"));
    }
}
