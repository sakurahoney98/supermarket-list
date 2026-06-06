![banner-supermarket](https://github.com/sakurahoney98/supermarket-list/blob/main/docs/banner.png)

# 🛒 Sistema de Gestão de Estoque e Lista de Compras

Um sistema para gerenciamento de estoque e geração automática de listas de compras domésticas.


## Tecnologias

As seguintes tecnologias foram utilizadas na construção do projeto:

### Front-End
- HTML
- CSS
- Angular

### Back-End
- Java 21
- Spring Boot


## Pré-requisitos

### Ambiente local
- Java 21
- Maven 3.x (ou ./mvnw)
- Node 18+ (ou 20+)
- Angular CLI 17+ (ajuste pra sua versão)
- PostgreSQL 14+

### Ambiente de produção
- `Docker`
- `docker-compose`


## Instalação

Clone o repositório:

```shell
git clone https://github.com/sakurahoney98/supermarket-list.git
```

## Execução

### Em ambiente local
1. Acesse a pasta do backend e execute a API:

```shell
cd backend
mvn spring-boot:run
```

A API ficará disponível em: http://localhost:8080

2. Acesse a pasta do frontend, instale as dependências e execute a aplicação:

```shell
cd frontend
npm install
ng serve
```

A aplicação ficará disponível em: http://localhost:4200


### Em ambiente de produção
1. Copie o arquivo `env.dist` como `.env` e modifique o valor das váriaveis de acordo com seu ambiente.
```shell
cp env.dist .env
nano .env
```

2. Conceda permissão de execução ao script:

```shell
chmod +x docker-run.sh
```

3.Suba toda a aplicação:

```shell
./docker-run.sh --server
```

A aplicação ficará disponível em: http://<IP_DO_SERVIDOR>


## Comandos úteis

Recriar containers e reconstruir imagens:

```shell
./docker-run.sh --server
```

Forçar rebuild completo das imagens

```shell
./docker-run.sh --clean
```

Remover volumes e recriar o banco de dados

⚠️ Este comando remove todos os dados persistidos.

```shell
./docker-run.sh --reset-db
```



## Contribuindo

Contribuições são sempre bem-vindas!

Veja o  [manual de contribuição](CONTRIBUTING.md) para saber como começar.


## Conecte-se comigo
[![Email](https://img.shields.io/badge/Email-red?style=for-the-badge&logo=gmail&logoColor=white)](mailto:caroline.santana@ucsal.edu.br) [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/caroline-santana-36378215a/)
