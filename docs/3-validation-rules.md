# Regras de Validação

## 1. Cadastro e Edição de Itens

### 1.1. Campos obrigatórios
Os seguintes campos **não podem ficar em branco**:
- Nome
- Quantidade em estoque
- Categoria
- Duração (em dias)

### 1.2. Campos opcionais
- Peso ou unidade
- Quantidade máxima de compra
- Data da última compra

### 1.3. Regras específicas
- Ao cadastrar um item, o sistema deve verificar se existe **pelo menos uma categoria cadastrada**. Caso contrário, o cadastro é bloqueado com mensagem explicativa.
- Na edição, os campos obrigatórios devem permanecer preenchidos. O ID do item não pode ser alterado.
- Qualquer tentativa de deixar um campo obrigatório em branco deve exibir um **feedback visual claro** (ex: borda vermelha, mensagem de erro).

---

## 2. Cadastro de Categorias

### 2.1. Campos obrigatórios
- Nome da categoria
- Cor da letra (formato hexadecimal ou nome de cor suportado)
- Cor do fundo (formato hexadecimal ou nome de cor suportado)

### 2.2. Pré-visualização
Durante o cadastro, o sistema deve mostrar uma **pré-visualização em tempo real** de como o nome da categoria aparecerá com as cores escolhidas (texto colorido sobre fundo colorido).

---

## 3. Exclusão em Massa

### 3.1. Itens
- O sistema deve permitir a exclusão de múltiplos itens de uma só vez (seleção por checkboxes).
- A exclusão deve ser **lógica** (soft delete) e uma mensagem de confirmação deve ser exibida antes da ação.

### 3.2. Categorias
- A exclusão em massa de categorias é permitida, mas **totalmente bloqueada** se pelo menos uma das categorias selecionadas estiver vinculada a algum item.
- Nesse caso, o sistema exibe a lista das categorias problemáticas e impede a exclusão.

---

## 4. Atualização Pós-Compra

- Os campos **marca** e **preço** são opcionais.
- Ao finalizar a compra (botão "Finalizar"), o sistema deve:
  - Atualizar o estoque dos itens (reduzir pela quantidade comprada) – *se essa for a regra definida pelo produto; caso contrário, apenas registrar a compra sem mexer no estoque*.
  - Inserir um registro na tabela de compras (ver `05-data-rules.md`).
  - Apenas itens com alteração de quantidade ou com marca/preço informados devem ser processados.