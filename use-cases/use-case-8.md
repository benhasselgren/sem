# USE CASE: 8 Delete an employee's details

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *HR Advisor* I want to delete an employee's details so that the company is compliant with data retention legislation.

### Level

Primary task.

### Preconditions

We know the employee. Database contains all the employee's.

### Success End Condition

An HR Advisor has successfully deleted an employee from database to comply with DRL.

### Failed End Condition

HR Advisor cannot delete employee.

### Primary Actor

HR Advisor.

### Trigger

A request to delete employee is sent to HR Advisor.

## MAIN SUCCESS SCENARIO

1. Request to delete employee is sent to HR Advisor.
2. HR Advisor deletes employee.

## EXTENSIONS

2. **Delete not succesfull**:
    1. HR advisor informs company delete not working.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0