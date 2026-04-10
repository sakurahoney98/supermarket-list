# Regras de Persistência e Modelo de Dados - Supermarket List

## 1. Modelo Físico (PostgreSQL)

### 1.1. Tabela `categoria`

| Campo | Tipo | Descrição |
|-------|------|-------------|
| `ide_categoria` | SERIAL PRIMARY KEY | Identificador único |
| `dsc_categoria` | VARCHAR(100) NOT NULL | Nome da categoria |
| `dtc_criacao` | DATE NOT NULL | Data de criação |
| `dtc_exclusao` | DATE | Data de exclusão lógica |
| `ind_ativo` | BOOLEAN DEFAULT TRUE | Ativo/inativo |
| `cor_letra` | VARCHAR(20) NOT NULL | Cor do texto |
| `cor_fundo` | VARCHAR(20) NOT NULL | Cor de fundo |

### 1.2. Tabela `item`

| Campo | Tipo | Descrição |
|-------|------|-------------|
| `ide_item` | SERIAL PRIMARY KEY | Identificador único |
| `nome_item` | VARCHAR(100) NOT NULL | Nome do item |
| `unidade_medida` | VARCHAR(50) | Peso ou unidade |
| `quantidade_estoque` | INTEGER NOT NULL | Quantidade atual |
| `li_mite_de_compra` | INTEGER NOT NULL | Quantidade máxima de compra |
| `data_ultima_compra` | DATE | Última compra registrada |
| `ide_categoria` | INTEGER NOT NULL REFERENCES categoria(ide_categoria) | Chave estrangeira |
| `duracao_dias` | INTEGER NOT NULL | Duração em dias (RN23) |
| `dtc_criacao` | DATE NOT NULL | Data de criação |
| `dtc_exclusao` | DATE | Data de exclusão lógica |
| `ind_ativo` | BOOLEAN DEFAULT TRUE | Ativo/inativo |
| **UNIQUE** | `(nome_item, ide_categoria)` | Garante RN20 |

### 1.3. Tabela `compra`

| Campo | Tipo | Descrição |
|-------|------|-------------|
| `ide_compra` | SERIAL PRIMARY KEY | Identificador único |
| `data_compra` | DATE NOT NULL | Data da ida ao mercado |
| `valor_total` | NUMERIC(10,2) | Valor total da compra (opcional, pode ser calculado) |

### 1.4. Tabela `item_compra` (associativa)

| Campo | Tipo | Descrição |
|-------|------|-------------|
| `ide_item_compra` | SERIAL PRIMARY KEY | Identificador único |
| `ide_compra` | INTEGER NOT NULL REFERENCES compra(ide_compra) | Chave estrangeira para compra |
| `ide_item` | INTEGER NOT NULL REFERENCES item(ide_item) | Chave estrangeira para item |
| `quantidade` | INTEGER NOT NULL | Quantidade comprada |
| `preco` | NUMERIC(10,2) | Preço unitário ou total (definir) |
| `marca` | VARCHAR(100) | Marca do produto |

### 1.5. Regras de integridade

- Soft delete via `ind_ativo` e `dtc_exclusao` (itens e categorias).
- Exclusão lógica em cascata? Não, deve ser tratada pela aplicação.
- A RN60 (soma de compras com mesma data) é implementada via lógica de aplicação, não por constraint.

