# sample-loan-app
A Spring Boot REST service that evaluates loan applications and determines whether a single loan offer (based on requested tenure) can be approved.

## Requirements

- Java 17 or newer
- Gradle installed locall

## Build

From the project root:

```bash
gradle clean build
```

This compiles the application and runs the unit tests.

## Run

Start the application with:

```bash
gradle bootRun
```
The service starts on:

```text
http://localhost:8080
```

Sample curl

```curl
curl --location 'http://localhost:8080/applications' \
--header 'Content-Type: application/json' \
--data '{
    "applicant": {
        "name": "string",
        "age": 30,
        "monthlyIncome": 75000,
        "employmentType": "SALARIED",
        "creditScore": 720
    },
    "loan": {
        "amount": 500000,
        "tenureMonths": 36,
        "purpose": "PERSONAL"
    }
}'
```


The main endpoint is:

```text
POST /applications
```

## Run Test
```bash
gradle test --console=plain
```
