# Vulnerable SPA

## Modern DevSecOps Security Benchmark for React and Spring Boot Applications

Vulnerable SPA is an intentionally vulnerable Single Page Application (SPA) built with React and Spring Boot.

The project is designed as an open benchmark platform for evaluating and comparing modern Application Security and DevSecOps tools, including:

* SAST (Static Application Security Testing)
* DAST (Dynamic Application Security Testing)
* SCA (Software Composition Analysis)
* Container Security
* Secret Scanning
* AI-Assisted Security Review

The application contains intentionally introduced security weaknesses based on common OWASP Top 10 categories and provides a realistic environment for security testing, secure coding exercises, and DevSecOps pipeline validation.

---

## Project Goals

* Demonstrate common web application vulnerabilities
* Provide a reproducible benchmark for security tools
* Support AppSec and DevSecOps training
* Validate CI/CD security pipelines
* Evaluate AI-powered security analysis solutions
* Practice secure coding and remediation techniques

---

## Technology Stack

### Frontend

* React
* JavaScript
* React Router

### Backend

* Java 21
* Spring Boot 3
* Spring Security
* Maven

### Database

* H2 Database

### Security Tooling

* Semgrep
* SonarQube
* Trivy
* Docker Scout
* Gitleaks
* OWASP ZAP

---

## Repository Structure

```text
vulnerable-spa/
├── backend/
│   ├── src/main/java/
│   │   └── com/example/vulnerablespa/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── model/
│   │       ├── repository/
│   │       └── security/
│   └── src/main/resources/
│       ├── db/migration/
│       ├── static/
│       └── application.properties
│
├── frontend/
│   ├── public/
│   └── src/
│       ├── components/
│       ├── pages/
│       └── utils/
│
├── .github/
│   └── workflows/
│
├── docs/
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Architecture Documentation

* Build Architecture: `docs/Maven-build.md`
* Vulnerability Documentation: `docs/`
* Security Pipelines: `.github/workflows/`

---

## Running the Project

### Requirements

* Java 21+
* Maven 3.9+
* Node.js 20+
* Docker

---

### Run with Docker

```bash
git clone https://github.com/kremlsa/vuln-spa.git

cd vuln-spa

docker build -t vuln-spa .

docker run \
  -e WAF_LEVEL=ADVANCED \
  -p 8080:8080 \
  vuln-spa
```

Available WAF modes:

```text
NONE
BASIC
ADVANCED
```

Application URL:

```text
http://localhost:8080
```

---

### Run Locally

Build frontend:

```bash
cd frontend
npm install
npm run build
```

Build backend:

```bash
mvn clean install
```

Run application:

```bash
mvn spring-boot:run
```

---

## Features

* Authentication and session management
* User and administrator roles
* Notes CRUD operations
* Search functionality
* Cookie-based sessions
* Demonstration WAF
* REST API endpoints
* Security testing scenarios

Default credentials:

```text
admin / admin
user / user
```

---

## Security Benchmark Scenarios

The project currently contains intentionally vulnerable implementations for educational and benchmarking purposes.

### Implemented Vulnerabilities

| ID     | Category                   | OWASP |
| ------ | -------------------------- | ----- |
| VS-001 | Cross-Site Scripting (XSS) | A03   |
| VS-002 | Broken Authentication      | A07   |
| VS-003 | SQL Injection              | A03   |

Documentation:

* XSS: `docs/XSS.md`
* Broken Authentication: `docs/Broken-Authentication.md`
* SQL Injection: `docs/SQL-Injection.md`

---

## DevSecOps Pipelines

The repository includes security pipelines for:

### Secret Scanning

* Gitleaks

### Static Analysis

* Semgrep
* SonarQube

### Dependency Analysis

* Trivy

### Container Security

* Trivy Container Scan
* Docker Scout

### Dynamic Analysis

* OWASP ZAP

---

## Intended Use Cases

### Security Training

Learn common web application vulnerabilities and remediation techniques.

### Tool Evaluation

Compare:

* Semgrep
* SonarQube
* CodeQL
* OWASP ZAP
* Trivy
* Docker Scout
* AI Security Agents

### CI/CD Validation

Test security gates and DevSecOps workflows before production adoption.

### Research

Evaluate AI-assisted vulnerability detection and security review capabilities.

---

## Disclaimer

This application intentionally contains security vulnerabilities.

DO NOT deploy it to production environments.

The project is intended solely for:

* Security education
* Tool benchmarking
* Research
* Training labs

---

## Contributing

Contributions are welcome.

Possible contribution areas:

* New vulnerability scenarios
* Additional benchmark cases
* Security tooling integrations
* Documentation improvements
* CI/CD enhancements

Please see CONTRIBUTING.md for details.

---

## License

Licensed under the Apache License 2.0.

See LICENSE for details.

---

## Author

Alexander Kremlev

GitHub:
https://github.com/kremlsa
