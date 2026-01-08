# Auth Lambda – Autenticação de Clientes

## Descrição

Esta Lambda tem como objetivo autenticar clientes e emitir um JWT válido para acesso à API principal.

A autenticação só é permitida se o cliente possuir uma Ordem de Serviço (OS) em aberto no banco de dados. Caso contrário, o acesso é negado.

A função é exposta via API Gateway e atua como o ponto central de autenticação para os clientes.

---

## Fluxo de Autenticação

1. O cliente faz uma requisição HTTP para a **API Gateway**
2. A API Gateway aciona a **Auth Lambda**
3. A Lambda:
    - Consulta o banco de dados (RDS PostgreSQL)
    - Verifica se o cliente possui uma OS em aberto
4. Se válido:
    - Gera e retorna um **JWT**
5. Se inválido:
    - Retorna erro de autenticação

---

## Endpoint

- **Path:** `/token`
- **Method:** `POST`
- **Responsabilidade:** Autenticar o cliente e retornar um JWT válido para acesso à API.

---

## Tecnologias Utilizadas

- **Java 21**
- **AWS Lambda**
- **AWS API Gateway**
- **AWS SAM**
- **PostgreSQL (RDS)**
- **AWS Secrets Manager**
- **JWT (HS256)**
- **GitHub Actions (CI/CD)**

---

## Executar localmente

Esta Lambda pode ser executada localmente utilizando o **AWS SAM**, simulando o API Gateway e o runtime da AWS.

### Pré-requisitos

- Java 21
- Docker
- AWS SAM CLI
- AWS CLI configurado (opcional, para acesso a recursos AWS)

---

### Passo a passo

Na raiz do projeto, execute:

```bash
sam build --use-container
```

Após o build, execute:

```bash
sam local start-api
```

A API ficará disponível em:

```bash
http://localhost:3000
```

Endpoint disponível localmente:

```bash
POST /token
```

Durante a execução, a Lambda será executada dentro de um container Docker, simulando o ambiente real da AWS Lambda integrado ao API Gateway.
