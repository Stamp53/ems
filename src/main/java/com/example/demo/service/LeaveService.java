package com.example.demo.service;

import com.example.demo.dto.LeaveRequestCreateDto;
import com.example.demo.dto.LeaveRequestResponse;
import com.example.demo.dto.LeaveStatusUpdateRequest;
import com.example.demo.model.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveService {

    LeaveRequestResponse applyLeave(String employeeEmail, LeaveRequestCreateDto request);

    Page<LeaveRequestResponse> getMyLeaves(String employeeEmail, Pageable pageable);

    Page<LeaveRequestResponse> getAllLeaves(LeaveStatus status, Pageable pageable);

    Page<LeaveRequestResponse> getDepartmentLeaves(Long departmentId, Pageable pageable);

    LeaveRequestResponse reviewLeave(Long leaveId, String reviewerEmail, LeaveStatusUpdateRequest request);
}
