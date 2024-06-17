package com.davidmaisuradze.trainerworkservice.service;

import com.davidmaisuradze.trainerworkservice.dto.ActionType;
import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.entity.MonthlySummary;
import com.davidmaisuradze.trainerworkservice.entity.TrainerWorkSummary;
import com.davidmaisuradze.trainerworkservice.entity.YearlySummary;
import com.davidmaisuradze.trainerworkservice.repository.TrainerWorkSummaryRepository;
import com.davidmaisuradze.trainerworkservice.service.impl.TrainerWorkSummaryServiceImpl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@CucumberContextConfiguration
public class TrainerWorkSummaryServiceTest {
    @Autowired
    private TrainerWorkSummaryServiceImpl trainerWorkSummaryService;

    @Autowired
    private TrainerWorkSummaryRepository trainerRepository;

    @Before
    public void cleanDatabase() {
        trainerRepository.deleteAll();
    }

    @Given("the database contains a trainer with username {string} and the following yearly summaries:")
    public void givenTheDatabaseContainsATrainerWithUsernameAndTheFollowingYearlySummaries(
            String username,
            DataTable dataTable
    ) {
        TrainerWorkSummary trainer = new TrainerWorkSummary();
        trainer.setUsername(username);
        trainer.setYearlySummaries(new ArrayList<>());

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            int year = Integer.parseInt(columns.get("year"));
            Month month = Month.valueOf(columns.get("month"));
            int workingMinutes = Integer.parseInt(columns.get("workingMinutes"));

            YearlySummary yearlySummary = trainer.getYearlySummaries().stream()
                    .filter(ys -> ys.getYear() == year)
                    .findFirst()
                    .orElseGet(() -> {
                        YearlySummary ys = new YearlySummary();
                        ys.setYear(year);
                        ys.setMonthlySummaries(new ArrayList<>());
                        trainer.getYearlySummaries().add(ys);
                        return ys;
                    });

            MonthlySummary monthlySummary = new MonthlySummary();
            monthlySummary.setMonth(month);
            monthlySummary.setWorkingMinutes(workingMinutes);
            yearlySummary.getMonthlySummaries().add(monthlySummary);
        }

        trainerRepository.save(trainer);
    }

    @When("the workload is added with the following details:")
    public void whenTheWorkloadIsAddedWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            TrainerWorkloadRequest request = new TrainerWorkloadRequest();
            request.setUsername(columns.get("username"));
            request.setTrainingDate(LocalDate.parse(columns.get("trainingDate")));
            request.setDurationMinutes(Integer.parseInt(columns.get("durationMinutes")));
            request.setActionType(ActionType.valueOf(columns.get("actionType")));

            trainerWorkSummaryService.addWorkload(request);
        }
    }

    @When("the workload is deleted with the following details:")
    public void whenTheWorkloadIsDeletedWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            TrainerWorkloadRequest request = new TrainerWorkloadRequest();
            request.setUsername(columns.get("username"));
            request.setTrainingDate(LocalDate.parse(columns.get("trainingDate")));
            request.setDurationMinutes(Integer.parseInt(columns.get("durationMinutes")));
            request.setActionType(ActionType.valueOf(columns.get("actionType")));

            trainerWorkSummaryService.addWorkload(request);
        }
    }

    @Then("the trainer's monthly summary for JANUARY {int} should be updated to {int} minutes")
    public void thenTheTrainersMonthlySummaryShouldBeUpdated(int year, int expectedMinutes) {
        TrainerWorkSummary trainer = trainerRepository.findByUsername("john_doe").orElseThrow();
        YearlySummary yearlySummary = trainer.getYearlySummaries().stream()
                .filter(ys -> ys.getYear() == year)
                .findFirst()
                .orElseThrow();

        MonthlySummary monthlySummary = yearlySummary.getMonthlySummaries().stream()
                .filter(ms -> ms.getMonth() == Month.JANUARY)
                .findFirst()
                .orElseThrow();

        Assertions.assertEquals(expectedMinutes, monthlySummary.getWorkingMinutes());
    }
}
