# Factory Optimizer

Aplicação web para gerenciamento de matérias-primas e otimização de produção industrial, sugerindo o que produzir para maximizar o valor total de venda com base no estoque disponível.

## Tecnologias Utilizadas

- **Back-end:** Java 17 + Spring Boot 3.x
- **Front-end:** Vue.js 3 + Vite
- **Banco de Dados:** H2 (em memória)
- **Documentação:** Swagger UI

---

## Pré-requisitos

Certifique-se de ter instalado:

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Node.js 18+](https://nodejs.org/)

---

## Como rodar o projeto localmente

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/factory-optimizer.git
cd factory-optimizer
```

### 2. Inicie o Back-end
```bash
cd backend
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

O banco de dados é populado automaticamente com dados de exemplo na inicialização.

### 3. Inicie o Front-end

Abra um novo terminal:
```bash
cd frontend
npm install
npm run dev
```

A aplicação estará disponível em: `http://localhost:5173`

---

## URLs úteis

| Serviço | URL |
|---|---|
| Frontend | http://localhost:5173 |
| API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| H2 Console | http://localhost:8080/h2-console |

### Credenciais do H2 Console

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:factorydb` |
| Username | `sa` |
| Password | *(deixar em branco)* |

---

## Rodando os testes
```bash
cd backend
mvn test
```

---

## Funcionalidades

- ✅ CRUD de Matérias-Primas (código, nome, quantidade em estoque, unidade)
- ✅ CRUD de Produtos com composição de ingredientes
- ✅ Otimizador de Produção — sugere o que produzir para maximizar o valor total de venda
- ✅ Resolução de conflitos — prioriza produtos com maior retorno financeiro quando disputam a mesma matéria-prima
- ✅ Testes unitários cobrindo a lógica de otimização de produção
- ✅ Swagger UI para documentação da API
- ✅ Dados de exemplo carregados automaticamente na inicialização

---

## Estrutura do Projeto
```
factory-optimizer/
├── backend/
│   └── src/
│       ├── main/
│       │   ├── java/com/factory/
│       │   │   ├── config/           # Configurações (CORS)
│       │   │   ├── controller/       # Controllers REST
│       │   │   ├── converter/        # Conversores Entity <-> DTO
│       │   │   ├── dto/              # DTOs de requisição e resposta
│       │   │   ├── entity/           # Entidades JPA
│       │   │   ├── exception/        # Exceções customizadas e handler
│       │   │   ├── repository/       # Repositórios Spring Data
│       │   │   └── service/          # Regras de negócio
│       │   └── resources/
│       │       ├── application.properties
│       │       └── data.sql          # Dados de exemplo
│       └── test/
│           └── java/com/factory/
│               └── service/          # Testes unitários
└── frontend/
    └── src/
        ├── router/                   # Vue Router
        ├── services/                 # Configuração do Axios
        └── views/                    # Páginas da aplicação
```

---

## Como funciona o Otimizador

O algoritmo analisa o estoque atual e sugere quais produtos fabricar para obter o **maior valor total de venda** possível.

**Fluxo:**
1. Para cada produto, calcula quantas unidades podem ser produzidas com o estoque disponível
2. Calcula o valor total potencial de cada produto (unidades × preço)
3. Ordena os produtos do maior para o menor valor potencial
4. Aloca o estoque começando pelo produto mais lucrativo
5. Deduz os insumos consumidos e repete para os próximos produtos

Dessa forma, quando dois produtos disputam a mesma matéria-prima, o algoritmo sempre prioriza aquele que gera maior retorno financeiro.
