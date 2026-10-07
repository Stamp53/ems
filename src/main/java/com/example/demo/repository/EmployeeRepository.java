package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<Employee> findByActive(boolean active, Pageable pageable);
}
