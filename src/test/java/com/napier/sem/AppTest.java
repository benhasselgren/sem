package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AppTest
{
    //------------------------------------------------- Setup -------------------------------------------------
    static App app;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    @BeforeAll
    static void init()
    {
        app = new App();
    }

    //------------------------------------------------- DisplaSalaries() -------------------------------------------------

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        app.printSalaries(employess);
    }

    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }

    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

    //------------------------------------------------- DisplayEmployees() -------------------------------------------------

    @Test
    void displayEmployeeTestNull()
    {
        app.displayEmployee(null);
    }

    @Test
    void displayEmployee()
    {
        Department dept = new Department();
        dept.dept_name = "Sales";
        dept.dept_no = "d007";

        Employee manager = new Employee();
        manager.emp_no = 2;
        manager.first_name = "John";
        manager.last_name = "Brown";
        manager.title = "Manager";
        manager.salary = 77000;
        manager.dept_name = dept;

        dept.manager = manager;

        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        emp.manager = manager;
        emp.dept_name = dept;

        app.displayEmployee(emp);
    }
}
