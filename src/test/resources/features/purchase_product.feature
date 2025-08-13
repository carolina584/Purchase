Feature: purchase product
  as an app user,
  i want to see the products
  to buy
  Background:
    Given user enters the application

  Scenario Outline: add product car and purchase
    When user searches <product> and adds product to cart
    And  complete checkout as a guest
    Then It shows that your order has been placed <detail>

    Examples:
      | product | detail                      |
      | MacBook | Your order has been placed! |