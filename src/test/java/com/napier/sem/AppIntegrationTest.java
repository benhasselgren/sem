package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060");
    }

    //------------------testGetEmployee()------------------
    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530, false, null);
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");
    }

    //------------------testDepartment()------------------
    @Test
    void testGetDepartment()
    {
        Department dept = app.getDepartment("Sales");

        assertEquals("Sales", dept.dept_name);
        assertEquals("Hauke", dept.manager.first_name);
        assertEquals("d007", dept.dept_no);
    }

    //------------------getAllSalaries()------------------
    @Test
    void testGetAllSalaries()
    {
        ArrayList<Employee> employees = app.getAllSalaries();

        assertEquals(240124, employees.size());
    }

    //------------------testGetAllSalariesWithRole()------------------
    @Test
    void testGetAllSalariesWithRole()
    {
        ArrayList<Employee> employees = app.getSalariesWithRole("Engineer");

        assertEquals(30983, employees.size());
    }

    //------------------testGetAllSalariesByDepartment()------------------
    @Test
    void testGetAllSalariesByDepartment()
    {
        ArrayList<Employee> employees = app.getSalariesByDepartment(app.getDepartment("Sales"));

        assertEquals(42000, employees.size());
    }

    //------------------testAddEmployee()------------------
    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        app.addEmployee(emp);

        emp = app.getEmployee(500000, true, null);
        assertEquals(emp.emp_no, 500000);
        assertEquals(emp.first_name, "Kevin");
        assertEquals(emp.last_name, "Chalmers");
    }
}
