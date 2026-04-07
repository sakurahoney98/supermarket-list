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

### 1.1. Regras Gerais

- Campos obrigatórios não podem ser deixados em branco.
- O sistema deve exibir feedback visual em caso de erro.
- O sistema deve impedir o cadastro de itens caso não existam categorias cadastradas.

---

## 2. Edição de Itens

- Todos os campos obrigatórios devem continuar preenchidos.
- O ID do item não pode ser alterado.

---

## 3. Exclusão de Itens

- Exclusão deve ser lógica (soft delete).


## 4. Cadastro de Categoria

Campos obrigatórios:
- Nome
- Cor da letra
- Fundo da forma


---

### 4.1. Regras Gerais

- Campos obrigatórios não podem ser deixados em branco.
- O sistema deve exibir feedback visual em caso de erro.

---


## 5. Exclusão de Categoria

- Exclusão deve ser lógica (soft delete).
- Exclusão de categoria deve ser bloqueada se houver itens vinculados.