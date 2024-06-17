Feature: Receive message from ActiveMQ

  Scenario: Test if the message is received
    Given an ActiveMQ broker is running
    When a message is sent to the queue
    Then the message listener should receive the message
