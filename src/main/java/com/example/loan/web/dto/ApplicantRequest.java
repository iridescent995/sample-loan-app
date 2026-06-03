package com.example.loan.web.dto;

import com.example.loan.domain.EmploymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ApplicantRequest(
        @NotBlank(message = "applicant.name is required")
        String name,

        @NotNull(message = "applicant.age is required")
        @Min(value = 21, message = "applicant.age must be at least 21")
        @Max(value = 60, message = "applicant.age must be at most 60")
        Integer age,

        @NotNull(message = "applicant.monthlyIncome is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "applicant.monthlyIncome must be greater than 0")
        BigDecimal monthlyIncome,

        @NotNull(message = "applicant.employmentType is required")
        EmploymentType employmentType,

        @NotNull(message = "applicant.creditScore is required")
        @Min(value = 300, message = "applicant.creditScore must be at least 300")
        @Max(value = 900, message = "applicant.creditScore must be at most 900")
        Integer creditScore
) {
}
