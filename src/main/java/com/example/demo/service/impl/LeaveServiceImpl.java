package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestCreateDto;
import com.example.demo.dto.LeaveRequestResponse;
import com.example.demo.dto.LeaveStatusUpdateRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.LeaveStatus;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LeaveRepository;
import com.example.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public LeaveRequestResponse applyLeave(String employeeEmail, LeaveRequestCreateDto request) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        LeaveRequest saved = leaveRepository.save(leaveRequest);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getMyLeaves(String employeeEmail, Pageable pageable) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return leaveRepository.findByEmployeeId(employee.getId(), pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getAllLeaves(LeaveStatus status, Pageable pageable) {
        if (status != null) {
            return leaveRepository.findByStatus(status, pageable).map(this::mapToResponse);
        }
        return leaveRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getDepartmentLeaves(Long departmentId, Pageable pageable) {
        return leaveRepository.findByEmployeeDepartmentId(departmentId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public LeaveRequestResponse reviewLeave(Long leaveId, String reviewerEmail, LeaveStatusUpdateRequest request) {
        LeaveRequest leaveRequest = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

        Employee reviewer = employeeRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave request is already " + leaveRequest.getStatus());
        }

        if (request.getStatus() == LeaveStatus.REJECTED && (request.getRejectionReason() == null || request.getRejectionReason().blank())) {
            throw new BadRequestException("Rejection reason is required when rejecting a leave");
        }

        leaveRequest.setStatus(request.getStatus());
        leaveRequest.setRejectionReason(request.getRejectionReason());
        leaveRequest.setReviewedBy(reviewer);

        LeaveRequest updated = leaveRepository.save(leaveRequest);
        return mapToResponse(updated);
    }

    private LeaveRequestResponse mapToResponse(LeaveRequest leave) {
        Employee emp = leave.getEmployee();
        Employee reviewer = leave.getReviewedBy();
        return LeaveRequestResponse.builder()
                .id(leave.getId())
                .employeeId(emp.getId())
                .employeeName(emp.getFirstName() + " " + emp.getLastName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .rejectionReason(leave.getRejectionReason())
                .reviewedByName(reviewer != null ? reviewer.getFirstName() + " " + reviewer.getLastName() : null)
                .createdAt(leave.getCreatedAt())
                .build();
    }
}
