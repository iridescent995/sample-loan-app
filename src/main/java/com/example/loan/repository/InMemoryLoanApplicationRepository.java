package com.example.loan.repository;

import com.example.loan.domain.LoanDecision;
import com.example.loan.domain.StoredLoanApplication;
import com.example.loan.web.dto.CreateLoanApplicationRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLoanApplicationRepository implements LoanApplicationRepository {

    private final ConcurrentMap<UUID, StoredLoanApplication> applications = new ConcurrentHashMap<>();

    @Override
    public StoredLoanApplication save(CreateLoanApplicationRequest request, LoanDecision decision) {
        UUID applicationId = UUID.randomUUID();
        StoredLoanApplication application = new StoredLoanApplication(
                applicationId,
                request,
                decision,
                Instant.now()
        );
        applications.put(applicationId, application);

        return application;
    }

    @Override
    public Optional<StoredLoanApplication> findById(UUID applicationId) {
        return Optional.ofNullable(applications.get(applicationId));
    }

    @Override
    public List<StoredLoanApplication> findAll() {
        return List.copyOf(new ArrayList<>(applications.values()));
    }
}
