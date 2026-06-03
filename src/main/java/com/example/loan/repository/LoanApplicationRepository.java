package com.example.loan.repository;

import com.example.loan.domain.LoanDecision;
import com.example.loan.domain.StoredLoanApplication;
import com.example.loan.web.dto.CreateLoanApplicationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanApplicationRepository {

    StoredLoanApplication save(CreateLoanApplicationRequest request, LoanDecision decision);

    Optional<StoredLoanApplication> findById(UUID applicationId);

    List<StoredLoanApplication> findAll();
}
