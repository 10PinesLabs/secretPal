package com.tenPines.builder;

import com.github.javafaker.Faker;
import com.tenPines.model.Worker;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.Month;


public class WorkerBuilder {

    private Faker faker = new Faker();
    private String fullName = faker.name().fullName();
    private String email = faker.internet().emailAddress();
    private LocalDate birthdayDate = LocalDate.of(1993, Month.APRIL,12);
    private Boolean wantsToParticipate = true;

    public WorkerBuilder withFullName(String fullName) {
        checkIfFullNameIsValid(fullName);
        this.fullName = fullName;
        return this;
    }

    public WorkerBuilder withEmail(String email) {
        checkIfValidEmail(email);
        this.email = email;
        return this;
    }

    public WorkerBuilder withBirthDayDate(LocalDate date) {
        this.birthdayDate = date;
        return this;
    }

    public WorkerBuilder whoDoesentWantToParticipate() {
        this.wantsToParticipate = false;
        return this;
    }

    public Worker build(){
        return new Worker(fullName,email,birthdayDate, wantsToParticipate);
    }

    public Worker buildFromDate(int day, Month month) {
        return new Worker(faker.name().fullName(),faker.internet().emailAddress(), LocalDate.of(1800, month.getValue(), day), wantsToParticipate);
    }

    private void checkIfFullNameIsValid(String fullName) {
        checkIfIsAValidName(fullName, "Full Name is invalid");
    }

    private void checkIfIsAValidName(String name, String message) {
        checkIfFieldIsValidUponCondition(StringUtils.isBlank(name) || !name.matches("[a-zA-Z ,.'-]+"), message);
    }

    private void checkIfValidEmail(String email) {
        checkIfFieldIsValidUponCondition(!EmailValidator.getInstance().isValid(email), "Email is invalid");
    }

    private void checkIfFieldIsValidUponCondition(Boolean condition, String message) {
        if (condition) throw new RuntimeException(message);
    }
}
