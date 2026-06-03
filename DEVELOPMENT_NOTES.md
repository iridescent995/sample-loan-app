# Development Notes

## Overall Approach

The assignment is mostly about applying business rules correctly, so I did not add a database, authentication, extra endpoints, or a large framework around the core flow.

The main path is:

1. `POST /applications` receives a loan application.
2. Added validations for each payload level args. 
3. `LoanEvaluationService` applies the eligibility and offer rules.
4. The decision is saved in an `in-memory` repository for audit purposes.
5. The controller returns either one offer or the rejection reasons.

## Key Design Decisions

- I kept request validation in the DTO layer using Jakarta Bean Validation. That keeps invalid input out of the business service.
- I used `BigDecimal` for EMI, interest, and total payable calculations, with scale `2` and `RoundingMode.HALF_UP` where money is returned.
- I split EMI calculation, risk classification, interest calculation, and full eligibility evaluation into separate services. That made the rules easier to test and avoids one large service method doing everything.
- I kept the controller thin. It receives the request, asks the service for a decision, stores it, and maps the result to the API response.
- I used an in-memory repository because the requirement says decisions should be stored for audit, but it does not require persistent storage.
- I added logs around useful business events, but avoided logging applicant names or full request bodies.

## Trade-Offs Considered

- A database would be better for real storage, that can be easily done by adding JDBC and repo config. For this sample, an in-memory repository keeps the focus on the loan rules.
- I returned rejection reasons as enum values. This is less friendly than full sentences, but it is stable for clients and easy to test.
- I did not add a read endpoint for stored applications. The repository is there, but the assignment only asks for creating and evaluating an application.
- I used normal Spring logging instead, it can be changed to centralised logging later.

## Assumptions Made

- The 60% EMI eligibility rule uses the base `12%` annual interest rate mentioned in the assignment.
- The final 50% offer rule uses the final calculated interest rate after all premiums are applied.
- The age-tenure rule rejects only when `age + tenure in years` is greater than `65`. If it is exactly `65`, I treat it as allowed.
- Only one offer is generated, and it always uses the requested tenure.
- Since the API contract shows `riskBand: null` for rejected applications, the response keeps that field as `null` when rejected.

## Improvements With More Time

- Add a real database table for applications and decisions.
- Add integration tests for `POST /applications`, including validation errors and rejected responses.
- Add API documentation so the request and response contract is easy to inspect.
- Add a read endpoint for looking up by application ID.
- Better logging.