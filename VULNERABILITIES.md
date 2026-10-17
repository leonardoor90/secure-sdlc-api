# 🛡️ Relatório de Vulnerabilidades e Remediação (Secure SDLC)

Este documento detalha as vulnerabilidades intencionais inseridas na branch `insecure-demo` e como elas foram corrigidas na branch `main`. O pipeline de CI/CD atua como o "Security Gate", bloqueando o merge de código inseguro.

---

## 1. Vazamento de Segredos (Hardcoded Secrets)
| Etapa | Descrição |
| :--- | :--- |
| **Finding (Achado)** | Chaves de API (AWS/Slack) e senhas inseridas diretamente no código e em arquivos `.env` commitados. |
| **Risk (Risco)** | **Crítico**. Atacantes podem usar as chaves para acessar serviços em nuvem, gerando custos ou vazamento de dados. |
| **Evidence (Evidência)** | Arquivo `.env` com `AWS_ACCESS_KEY_ID=AKIA...` ou `AuthControllerVulnerable.java` com `JWT_SECRET="..."`. |
| **Remediation (Remediação)** | Remoção dos segredos do código. Uso de variáveis de ambiente (`${JWT_SECRET}`) e GitHub Secrets no pipeline. |
| **Validation (Validação)** | A ferramenta **TruffleHog** falha o pipeline na branch `insecure-demo` e passa na `main`. |

---

## 2. SQL Injection
| Etapa | Descrição |
| :--- | :--- |
| **Finding (Achado)** | Concatenação direta de entrada do usuário na query SQL. |
| **Risk (Risco)** | **Alto**. Permite que atacantes leiam, alterem ou deletem dados do banco de dados. |
| **Evidence (Evidência)** | `String query = "SELECT * FROM users WHERE name LIKE '%" + name + "%'";` |
| **Remediation (Remediação)** | Uso de JPA/Hibernate e Prepared Statements (evitando concatenação de strings). |
| **Validation (Validação)** | A ferramenta **Semgrep (SAST)** detecta o padrão de concatenação e falha o pipeline. |

---

## 3. Mass Assignment
| Etapa | Descrição |
| :--- | :--- |
| **Finding (Achado)** | Atualização de campos sensíveis (como `role`) sem validação ou DTOs apropriados. |
| **Risk (Risco)** | **Alto**. Um usuário comum pode se promover a ADMIN modificando o JSON da requisição. |
| **Evidence (Evidência)** | `@PutMapping` recebendo um `Map<String, Object>` sem filtro de campos. |
| **Remediation (Remediação)** | Uso de DTOs específicos com `@Valid` e restrição de campos permitidos. |
| **Validation (Validação)** | Testes de integração demonstram a rejeição de campos não permitidos. |

---

## 4. Endpoint sem Autorização (Broken Access Control)
| Etapa | Descrição |
| :--- | :--- |
| **Finding (Achado)** | Endpoint administrativo (`/api/vulnerable/admin/users`) acessível sem verificação de role. |
| **Risk (Risco)** | **Alto**. Exposição de dados de todos os usuários. |
| **Evidence (Evidência)** | `@GetMapping("/admin/users")` sem `@PreAuthorize("hasRole('ADMIN')")`. |
| **Remediation (Remediação)** | Inclusão de anotações do Spring Security (`@PreAuthorize`) e configuração no `SecurityConfig`. |
| **Validation (Validação)** | Testes automatizados (curl com token de USER) retornam `403 Forbidden`. |

---

## 5. Log de Dados Sensíveis
| Etapa | Descrição |
| :--- | :--- |
| **Finding (Achado)** | Impressão de tokens JWT em logs de console. |
| **Risk (Risco)** | **Médio**. Vazamento de tokens em ferramentas de monitoramento (ELK, Splunk). |
| **Evidence (Evidência)** | `log.info("User logged in with token: " + token);` |
| **Remediation (Remediação)** | Remoção do token do log, mantendo apenas o `username`. |
| **Validation (Validação)** | Revisão manual e análise de logs em produção. |

---

## 📊 Resumo dos Resultados do Pipeline

| Ferramenta | Categoria | Branch `insecure-demo` | Branch `main` |
| :--- | :--- | :--- | :--- |
| **TruffleHog** | Secret Scanning | ❌ Falha (Acha segredos) | ✅ Passa |
| **Semgrep** | SAST | ❌ Falha (Acha SQLi) | ✅ Passa |
| **Dependency-Check** | SCA | ⚠️ Falha (Sem API Key) | ⚠️ Falha (Sem API Key) |
| **Trivy** | Container Scan | ❌ Bloqueado (Pipeline parou antes) | ✅ Passa |
