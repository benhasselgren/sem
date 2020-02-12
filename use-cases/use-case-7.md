# USE CASE: 7 Update an employee's details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want to update an employee's details so that employee's details are kept up-to-date.

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the employee. Database contains current employee salary data.

### Success End Condition

An HR Advisor has successfully updated an employee's details to be able to keep everything up-to-date.


### Failed End Condition

Employee's details not updated.

### Primary Actor

HR Advisor.

### Trigger

A request to update employee's details is sent to HR Advisor.

## MAIN SUCCESS SCENARIO

1. Request to update employee's details is sent to HR Advisor.
2. HR Advisor updates details of employee.

## EXTENSIONS

2. **Update not succesfull**:
    1. HR advisor informs company update not working.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0