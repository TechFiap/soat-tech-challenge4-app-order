Feature: Order aggregate behavior
  The Order aggregate root must correctly calculate totals and manage status transitions.

  Scenario: Create an Order and calculate total
    Given an Order with the following items:
      | price | quantity |
      | 10.00 | 2        |
      | 5.50  | 1        |
    When the Order is created
    Then the Order total should be 25.50

  Scenario: Successfully update Order status
    Given an Order with initial status "CREATED"
    When I update the Order status to "RECEBIDO"
    Then the Order status should be "RECEBIDO"

  Scenario: Fail to update Order to an invalid status
    Given an Order with initial status "CREATED"
    When I update the Order status to "INVALID_STATUS"
    Then an IllegalArgumentException should be thrown

  Scenario: Fail to update Order to the same status
    Given an Order with initial status "CREATED"
    When I update the Order status to "CREATED"
    Then an ErrorException should be thrown