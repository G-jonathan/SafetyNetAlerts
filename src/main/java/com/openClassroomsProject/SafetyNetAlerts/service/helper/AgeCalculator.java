package com.openClassroomsProject.SafetyNetAlerts.service.helper;

import com.openClassroomsProject.SafetyNetAlerts.model.AgeCalculationModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AgeCalculator {
    
    public static int ageCalculator(AgeCalculationModel ageCalculationModel) {
        String pattern = ageCalculationModel.getPattern();
        String birthDate = ageCalculationModel.getBirthdate();
        LocalDate actualDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate birthdateLocalDate = LocalDate.parse(birthDate, formatter);
        long ageInNumberOfDays = ChronoUnit.DAYS.between(birthdateLocalDate, actualDate);
        return (int) ((int) ageInNumberOfDays / 365.25);
    }
}