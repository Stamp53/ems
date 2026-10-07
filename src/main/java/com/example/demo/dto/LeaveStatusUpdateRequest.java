package com.example.demo.dto;

import com.example.demo.model.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private LeaveStatus status;

    private String rejectionReason;
}
