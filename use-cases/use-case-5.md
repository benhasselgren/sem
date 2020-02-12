# USE CASE: 5 Add a new employee's details

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *HR Advisor* I want to add a new employee's details so that I can ensure the new employee is paid.

### Level

Primary task.

### Preconditions

We have the employee's details. Database contains lots of pre-existing employee's.

### Success End Condition

An HR Advisor has successfully added a new employee to the database.

### Failed End Condition

Employee is not added

### Primary Actor

HR Advisor.

### Trigger

A request to add a new employee is sent to the HR Advisor.

## MAIN SUCCESS SCENARIO

1. A request to add a new employee is sent.
2. HR Advisor captures details of new employee.
3. HR Advisor adds new emplyee to database.

## EXTENSIONS

3. **Employee not added.**:
    1. HR Advisor informs that the users details are not sufficient.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0