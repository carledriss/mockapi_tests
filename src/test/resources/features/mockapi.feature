Feature: MockApi tests

  @cleanData
  Scenario: POST Users
    When I send a POST request to "/Users" with json body
    """
    {
      "createdAt": 1631825833,
      "employee_firstname": "TestData12345",
      "employee_lastname": "TestData12345",
      "employee_phonenumbe": "264-783-9453",
      "ademployee_emaildress": "ademployee_emaildress",
      "citemployee_addressy": "citemployee_addressy",
      "stateemployee_dev_level": "stateemployee_dev_level",
      "employee_gender": "employee_gender",
      "employee_hire_date": "2025-10-31T16:35:45.426Z",
      "employee_onleave": true,
      "tech_stack": [],
      "project": []
    }
    """
    Then I validate the response has status code 201
    And I save the response as "User"
    And I save the request endpoint for deleting
    And I validate the response contains:
      | createdAt               | 1631825833               |
      | employee_firstname      | TestData12345            |
      | employee_lastname       | TestData12345            |
      | employee_phonenumbe     | 264-783-9453             |
      | ademployee_emaildress   | ademployee_emaildress    |
      | citemployee_addressy    | citemployee_addressy     |
      | stateemployee_dev_level | stateemployee_dev_level  |
      | employee_gender         | employee_gender          |
      | employee_hire_date      | 2025-10-31T16:35:45.426Z |
      | employee_onleave        | true                     |
      | tech_stack              | []                       |
      | project                 | []                       |
    And I send a GET request to "/Users"
    And I validate the response has status code 200
    And I validate the response array contains "id" equals "(User.id)"
    And I send a GET request to "/Users/{User.id}"
    And I validate the response has status code 200
    And I validate the response contains:
      | createdAt               | 1631825833               |
      | employee_firstname      | TestData12345            |
      | employee_lastname       | TestData12345            |
      | employee_phonenumbe     | 264-783-9453             |
      | ademployee_emaildress   | ademployee_emaildress    |
      | citemployee_addressy    | citemployee_addressy     |
      | stateemployee_dev_level | stateemployee_dev_level  |
      | employee_gender         | employee_gender          |
      | employee_hire_date      | 2025-10-31T16:35:45.426Z |
      | employee_onleave        | true                     |
      | tech_stack              | []                       |
      | project                 | []                       |
