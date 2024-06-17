Feature:
  Scenario: Send TrainerWorkloadRequest to ActiveMQ
    Given gym-rest-api is running
    When a TrainerWorkloadRequest is sent to ActiveMQ
    Then the message should be in the queue