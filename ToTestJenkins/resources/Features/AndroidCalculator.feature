#Author: sanosuke009@gmail.com
#Background: List of steps run before each of the scenarios

@AndroidCalculator
Feature: This Feature contains test scenarios of android calculator
  I want to test several scenarios regarding android calculator app
  
  Background:
		Given I want to open the "Calculator" in the "TA38501MVU" android device

        
@KeywordDriven @FirstScenario
  Scenario Outline: Test Case 1 : Perform Operation on Two Integers in Calculator
    Given I want to test the testcase for "<Keyword>"
    And I want the calculator to be opened in device
    When I tap number key "Number1" on keypad
    And I tap operator key "Icon1" on keypad
    And I tap number key "Number2" on keypad
    And I tap operator key "Icon2" on keypad
    Then I validate the result of "Number1" "Icon1" "Number2"

    Examples: 
      | Keyword  |
      | AndroidTest3 |
      
@KeywordDriven
  Scenario Outline: Test Case 2 : Perform Operation on Two Integers in Calculator
    Given I want to test the testcase for "<Keyword>"
    And I want the calculator to be opened in device
    When I tap number key "Number1" on keypad
    And I tap operator key "Icon1" on keypad
    And I tap number key "Number2" on keypad
    And I tap operator key "Icon2" on keypad
    Then I validate the result of "Number1" "Icon1" "Number2"

    Examples: 
      | Keyword  |
      | AndroidTest1 |
      | AndroidTest2 |
      | AndroidTest3 |
      | AndroidTest4 |
      
@KeywordDriven @LastScenario
  Scenario Outline: Test Case 2 : Perform Operation on Two Integers in Calculator
    Given I want to test the testcase for "<Keyword>"
    And I want the calculator to be opened in device
    When I tap number key "Number1" on keypad
    And I tap operator key "Icon1" on keypad
    And I tap number key "Number2" on keypad
    And I tap operator key "Icon2" on keypad
    Then I validate the result of "Number1" "Icon1" "Number2"

    Examples: 
      | Keyword  |
      | AndroidTest2 |
