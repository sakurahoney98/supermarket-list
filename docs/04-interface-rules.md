# Regras de Persistência e Modelo de Dados - Supermarket List

## 1. Tabela de Itens Comprados (Histórico)

**Conforme RN53:**  
Deve conter os campos:

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|------------|
| `id` | inteiro | Sim | Chave primária |
| `item_id` | inteiro | Sim | FK para tabela de itens |
| `data_compra` | data | Sim | Data da ida ao supermercado |
| `quantidade_comprada` | inteiro | Sim | Quantidade efetivamente comprada |
| `marca` | texto | Não | Marca do produto |
| `preco` | decimal(10,2) | Não | Preço (total ou unitário – definir) |

**Regra de inserção:**  
Ao clicar em "Finalizar" na tela pós-compra (RN50), o sistema deve inserir um registro nesta tabela para cada item cuja quantidade comprada > 0 (ou que teve marca/preço informados, conforme regra de negócio).

---

## 2. Atualização de Estoque Pós-Compra

- O estoque dos itens deve ser **reduzido** pela quantidade comprada no momento da finalização da compra.
- A operação (inserção no histórico + atualização do estoque) deve ser atômica.

---

## 3. Exclusão Lógica (Soft Delete) – RN27, RN38, RN58

- Tabelas `itens` e `categorias` devem ter uma coluna `deleted_at` (timestamp, nulo quando ativo) ou `is_active` (booleano).
- Listagens padrão e relatórios devem filtrar apenas registros com `deleted_at IS NULL` (ou `is_active = true`).

---

## 4. Persistência Seletiva (RN51)

- Na tela de atualização pós-compra, o backend deve processar **apenas os itens cuja quantidade foi alterada** (comparada com o valor original em estoque antes da compra).
- Itens sem alteração não geram INSERT/UPDATE.

---

## 5. Integridade Referencial

- A exclusão de categorias é bloqueada se houver itens vinculados (RN39/RN40) – deve ser verificada no backend.
- Itens com o mesmo nome dentro da mesma categoria são proibidos (RN20) – validação única composta.