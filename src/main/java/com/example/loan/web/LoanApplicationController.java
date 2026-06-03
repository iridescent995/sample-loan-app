package com.example.loan.web;

import com.example.loan.domain.LoanDecision;
import com.example.loan.domain.StoredLoanApplication;
import com.example.loan.repository.LoanApplicationRepository;
import com.example.loan.service.LoanEvaluationService;
import com.example.loan.web.dto.CreateLoanApplicationRequest;
import com.example.loan.web.dto.LoanApplicationResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private static final Logger log = LoggerFactory.getLogger(LoanApplicationController.class);

    private final LoanEvaluationService loanEvaluationService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationResponseMapper responseMapper;

    public LoanApplicationController(
            LoanEvaluationService loanEvaluationService,
            LoanApplicationRepository loanApplicationRepository,
            LoanApplicationResponseMapper responseMapper
    ) {
        this.loanEvaluationService = loanEvaluationService;
        this.loanApplicationRepository = loanApplicationRepository;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplicationResponse create(@Valid @RequestBody CreateLoanApplicationRequest request) {
        log.info(
                "Received loan application for a {} loan over {} months",
                request.loan().purpose(),
                request.loan().tenureMonths()
        );

        LoanDecision decision = loanEvaluationService.evaluate(request);
        StoredLoanApplication application = loanApplicationRepository.save(request, decision);

        log.info("Saved loan application {} with status {}", application.applicationId(), decision.status());
        return responseMapper.toResponse(application);
    }
}
