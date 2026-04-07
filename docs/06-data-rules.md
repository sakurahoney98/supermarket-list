# Regras de Persistência e Modelo de Dados

## 1. Tabela de Itens Comprados (Histórico)

O sistema deve armazenar cada compra finalizada na seguinte estrutura:

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|------------|
| `id` | inteiro | Sim | Chave primária (auto incremento) |
| `item_id` | inteiro | Sim | Referência ao item na tabela de itens (FK) |
| `data_compra` | data | Sim | Data em que a compra foi registrada (padrão: data atual) |
| `quantidade` | inteiro | Sim | Quantidade comprada |
| `marca` | texto | Não | Marca do produto |
| `preco` | decimal(10,2) | Não | Preço total pago (ou preço unitário – definir consistência) |

**Regra de inserção:**  
Sempre que o usuário clicar em **Finalizar** na tela pós-compra, o sistema deve inserir um registro nesta tabela para **cada item que teve quantidade > 0** (ou que teve marca/preço informados, mesmo que quantidade zero? – sugerimos apenas quantidade > 0).

---

## 2. Atualização de Estoque Pós-Compra

- O estoque dos itens deve ser **reduzido** pela quantidade comprada no momento da finalização da compra.
- A operação deve ser atômica (inserção no histórico + atualização do estoque) para evitar inconsistências.

---

## 3. Exclusão Lógica (Soft Delete)

- As tabelas `itens` e `categorias` devem conter um campo `deleted_at` (timestamp, nulo quando ativo) ou `ativo` (booleano).
- Todas as consultas para listagem, busca, relatórios e cálculos devem filtrar apenas registros com `deleted_at IS NULL` (ou `ativo = true`).
- A exclusão em massa (RN-12 e RN-21) deve preencher `deleted_at` com a data/hora atual para todos os registros selecionados.

---

## 4. Persistência Seletiva (Performance)

- Nas telas de edição de estoque e pós-compra, o frontend deve enviar ao backend **apenas os itens cujos valores foram alterados**.
- O backend deve atualizar somente os registros recebidos, sem tocar nos demais.