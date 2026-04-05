# Regras de Validação

## 1. Cadastro de Itens

Campos obrigatórios:
- Nome
- Quantidade em estoque
- Categoria
- Duração

Campos opcionais:
- Peso ou unidade
- Quantidade máxima de compra
- Data da última compra

---

## 2. Regras Gerais

- Campos obrigatórios não podem ser deixados em branco.
- O sistema deve exibir feedback visual em caso de erro.
- O sistema deve impedir o cadastro de itens caso não existam categorias cadastradas.

---

## 3. Edição de Itens

- Todos os campos obrigatórios devem continuar preenchidos.
- O ID do item não pode ser alterado.

---

## 4. Exclusão

- Exclusão deve ser lógica (soft delete).
- Exclusão de categoria deve ser bloqueada se houver itens vinculados.