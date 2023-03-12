package com.example.springdemo2.repository;

import com.example.springdemo2.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
// aici nu trebuie creeate metode deoarece JpaRepository extinde la randul sau alte interfete cu implementari deja facute
}
