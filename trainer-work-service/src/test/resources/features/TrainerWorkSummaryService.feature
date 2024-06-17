Feature: Trainer work summary service
  Scenario: Add workload for a trainer
    Given the database contains a trainer with username "john_doe" and the following yearly summaries:
      | year | month      | workingMinutes |
      | 2023 | JANUARY    | 120            |
    When the workload is added with the following details:
      | username | trainingDate | durationMinutes | actionType |
      | john_doe | 2023-01-15   | 60              | ADD        |
    Then the trainer's monthly summary for JANUARY 2023 should be updated to 180 minutes

  Scenario: Delete workload for a trainer
    Given the database contains a trainer with username "john_doe" and the following yearly summaries:
      | year | month      | workingMinutes |
      | 2023 | JANUARY    | 120            |
    When the workload is deleted with the following details:
      | username | trainingDate | durationMinutes | actionType |
      | john_doe | 2023-01-15   | 60              | DELETE     |
    Then the trainer's monthly summary for JANUARY 2023 should be updated to 60 minutes