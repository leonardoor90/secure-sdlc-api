# 🛡️ Secure SDLC Pipeline — Spring Boot API

Este projeto demonstra a implementação de uma **API REST segura** utilizando Spring Boot 3, integrada a uma esteira **DevSecOps** completa no GitHub Actions. O objetivo é provar a transição de um código vulnerável para um código seguro, utilizando ferramentas de análise automática.

[![Secure SDLC Pipeline](https://github.com/leonardoor90/secure-sdlc-api/actions/workflows/secure-sdlc-pipeline.yml/badge.svg?branch=main)](https://github.com/leonardoor90/secure-sdlc-api/actions/workflows/secure-sdlc-pipeline.yml)

## 🎯 Objetivo e Metodologia
O projeto utiliza o conceito de **Shift-Left Security**, onde as falhas são detectadas o mais cedo possível no ciclo de desenvolvimento:
1. **`main`**: Código seguro, pronto para produção. O pipeline passa (✅).
2. **`insecure-demo`**: Código contendo vulnerabilidades intencionais. O pipeline é **bloqueado (❌)** pelas ferramentas de segurança.

Consulte o arquivo [VULNERABILITIES.md](./VULNERABILITIES.md) para ver o relatório completo de Finding → Risk → Evidence → Remediation.

## 🚀 Tech Stack e Ferramentas
- **Backend**: Java 21, Spring Boot 3, Spring Security (JWT + RBAC), H2 Database.
- **CI/CD**: GitHub Actions (Pipeline Unificado).
- **Segurança (DevSecOps)**:
  - **SAST**: Semgrep
  - **Secret Scanning**: TruffleHog
  - **SCA**: OWASP Dependency-Check
  - **Container Scan**: Trivy
- **Infraestrutura**: Docker (Multi-stage build).

## 🏗️ Arquitetura do Pipeline

```mermaid
graph LR
    A[Developer Push] --> B[1. Unit Tests]
    B --> C[2. SAST & Secrets]
    C --> D[3. SCA]
    D --> E[4. Build Docker]
    E --> F[5. Container Scan]
    F --> G{Security Gate}
    G -->|Pass| H[Deploy]
    G -->|Fail| I[Block Merge]