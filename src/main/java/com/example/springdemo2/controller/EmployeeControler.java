package com.example.springdemo2.controller;

import com.example.springdemo2.model.Employee;
import com.example.springdemo2.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// asta e cea mai imp chestie
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class EmployeeControler {

    // cu autowired injectam
    @Autowired
    private EmployeeRepository employeeRepository;

    // get all employees

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }


    // create Employee rest api
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

}
