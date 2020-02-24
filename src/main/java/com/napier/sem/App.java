package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        //Get all salaries
        //ArrayList<Employee> employees = a.getAllSalaries();

        //Get salaries by job title
        //ArrayList<Employee> employees = a.getSalariesWithRole();

        //Get salaries by department
        ArrayList<Employee> employees = a.getSalariesByDepartment(a.getDepartment( "Sales"));

        //Print the salaries
        a.printSalaries(employees);

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Gets all the employees and salaries which have <title>.
     * @param ID The employee id
     * @param isManager Checks to see if it's an employee or manager.
     * @param deptCopy A copy of the department
     * @return An employee.
     */
    public Employee getEmployee(int ID, boolean isManager, department deptCopy)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, titles.title, salaries.salary, departments.dept_name, (SELECT emp_no FROM employees WHERE emp_no = dept_manager.emp_no) AS manager FROM employees "
                            + "JOIN salaries on  employees.emp_no=salaries.emp_no "
                            + "JOIN titles on  employees.emp_no=titles.emp_no "
                            + "JOIN dept_emp on  employees.emp_no=dept_emp.emp_no "
                            + "JOIN departments on  dept_emp.dept_no=departments.dept_no "
                            + "JOIN dept_manager on  departments.dept_no=dept_manager.dept_no "
                            + "WHERE employees.emp_no = " + ID + " "
                            + "AND dept_emp.to_date = '9999-01-01' AND dept_manager.to_date = '9999-01-01' "
                            + "LIMIT 1; ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                //Get the employee details
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary =  rset.getInt("salaries.salary");
                emp.title = rset.getString("titles.title");

                if(isManager)
                {
                    //Add the department to the manager and then add the manager to the department
                    emp.dept_name = deptCopy;
                    emp.manager = null;
                    deptCopy.manager = emp;

                    //Return the manager
                    return emp;
                }

                //Get the department and get the manager
                department dept =  getDepartment(rset.getString("departments.dept_name"));

                Employee manager =  getEmployee(rset.getInt("manager"), true, dept);

                //Then add the department and manager to employee
                emp.dept_name = dept;
                emp.manager = manager;

                //Then return the employee
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     * @param emp The employee to print
     */
    public void displayEmployee(Employee emp)
    {
        // Check employees is not null
        if (emp != null)
        {
            String emp_string = String.format("Emp no: %-8s First name: %-8s Last name: %-8s Title: %-8s Salary: %-8s Department: %-8s Manager: %s %s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.title, emp.salary, emp.dept_name.dept_name, emp.manager.first_name, emp.manager.last_name);
            System.out.println(emp_string);
        }
        System.out.println("No employee");
        return;

    }

    /**
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Gets all the employees and salaries which have <title>.
     * @param title the job role
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getSalariesWithRole(String title)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries, titles "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = titles.emp_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "AND titles.to_date = '9999-01-01' "
                            + "AND titles.title = '" + title + "' "
                            + "ORDER BY employees.emp_no ASC ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    /**
     * Prints a list of employees.
     * @param dept The department the employees are in.
     * @return  List of employees
     */
    public ArrayList<Employee> getSalariesByDepartment(department dept)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries, dept_emp, departments "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = dept_emp.emp_no "
                            + "AND dept_emp.dept_no = departments.dept_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "AND departments.dept_no = '" + dept.dept_no + "' "
                            + "ORDER BY employees.emp_no ASC ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Returns a department.
     * @param dept_name The name of the department to return.
     * @return A department
     */
    public department getDepartment(String dept_name) {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT departments.dept_no, departments.dept_name, dept_manager.emp_no "
                            + "FROM departments, dept_manager "
                            + "WHERE departments.dept_no = dept_manager.dept_no "
                            + "AND dept_manager.to_date = '9999-01-01' "
                            + "AND departments.dept_name = '" + dept_name + "' ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                // Extract department information
                department dept = new department();
                dept.dept_no = rset.getString("departments.dept_no");
                dept.dept_name = rset.getString("departments.dept_name");
                //Manager will be set in the getEmployee() method
                dept.manager = getEmployee(rset.getInt("dept_manager.emp_no"), true, dept);

                //return the department
                return dept;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }
}
