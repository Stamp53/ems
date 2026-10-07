package com.example.demo.service.impl;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.dto.DepartmentResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponse(dept);
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department with name '" + request.getName() + "' already exists");
        }

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
        }

        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .manager(manager)
                .build();

        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department with name '" + request.getName() + "' already exists");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }

        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            throw new BadRequestException("Cannot delete department that has assigned employees");
        }

        departmentRepository.delete(department);
    }

    private DepartmentResponse mapToResponse(Department dept) {
        Employee manager = dept.getManager();
        return DepartmentResponse.builder()
                .id(dept.getId())
                .name(dept.getName())
                .description(dept.getDescription())
                .managerId(manager != null ? manager.getId() : null)
                .managerName(manager != null ? manager.getFirstName() + " " + manager.getLastName() : null)
                .employeeCount(dept.getEmployees() != null ? dept.getEmployees().size() : 0)
                .createdAt(dept.getCreatedAt())
                .build();
    }
}
