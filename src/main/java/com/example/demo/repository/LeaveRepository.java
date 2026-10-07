package com.example.demo.repository;

import com.example.demo.model.LeaveRequest;
import com.example.demo.model.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    Page<LeaveRequest> findByEmployeeDepartmentId(Long departmentId, Pageable pageable);
}
