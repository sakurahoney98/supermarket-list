# Regras de Negócio

## 1. Cálculo da Quantidade Sugerida de Compra

A quantidade sugerida de um item é determinada com base na duração (em dias) do item e nas quantidades em estoque e máxima.

### 1.1. Regra preliminar (RN16)
Se a quantidade em estoque for **maior ou igual** à quantidade máxima de compra, o item **não deve ser incluído** na lista de compras.

### 1.2. Itens com duração ≥ 30 dias (RN11)
- Se quantidade em estoque = 0 → quantidade sugerida = quantidade máxima de compra.
- Caso contrário (estoque > 0) → não comprar (quantidade sugerida = 0).

### 1.3. Itens com duração < 30 dias (RN12 a RN15)
Definir:
- `quantidade_base = quantidade_maxima - quantidade_estoque`
- `fator_duracao = ceil(30 / duracao)`  (arredondar para cima)
- `quantidade_por_duracao = fator_duracao - quantidade_estoque`

A **quantidade sugerida** é o **menor valor** entre `quantidade_base` e `quantidade_por_duracao`, com **mínimo 0** (não pode ser negativo).

> Exemplo: duração = 7, estoque = 2, máximo = 10  
> quantidade_base = 8  
> fator_duracao = ceil(30/7)=5 → quantidade_por_duracao = 5-2=3  
> quantidade_sugerida = min(8,3) = 3

---

## 2. Geração e Exibição da Lista de Compras

### 2.1. Pré-visualização da lista (RN1 a RN8)
- A pré-visualização exibe todos os itens cadastrados, separados por categoria e em ordem alfabética (RN1).
- Cada item mostra: nome, quantidade a ser comprada, quantidade em estoque, botões + / - / reset (RN2). Reset retorna à quantidade inicial (RN3).
- O usuário pode escolher visualizar: **todos os itens** ou **apenas os itens que serão incluídos na lista final** (RN4).
- Se a quantidade sugerida for 0, o item é removido da lista final (RN5).
- Botão "Salvar" → exporta estado atual em CSV ou JSON (RN6).
- Botão "Finalizar" → confirma e faz download da lista em PDF (RN7, RN8).

### 2.2. Regra de inclusão automática (RN9)
Itens com **quantidade em estoque igual a 0** devem ser automaticamente incluídos na lista de compras.

### 2.3. Exportação PDF (RN8)
O PDF final deve conter:
- Itens separados por categoria
- Ordem alfabética dentro de cada categoria
- Nome e quantidade a ser comprada

---

## 3. Exclusão Lógica (Soft Delete)

- Itens e categorias devem ser **excluídos logicamente** (RN27, RN38).  
- Registros excluídos logicamente **não aparecem nas listagens padrão** (RN58).  

---

## 4. Tela de Atualização de Estoque (RN41 a RN45)

- Exibe todos os itens cadastrados com: nome, categoria, quantidade em estoque.
- Permite aumentar, diminuir e resetar a quantidade.
- Botão "Salvar" → armazenamento temporário (CSV/JSON).
- Botão "Finalizar" → persiste no banco de dados.
- Listagem organizada por categoria e ordem alfabética.

---

## 5. Tela de Atualização Após Compra (RN46 a RN52)

- Deve conter: identificação da data de ida ao supermercado (RN47a).
- Lista de itens com nome, categoria e quantidade.
- Permite: alterar quantidade, resetar, informar marca (opcional), informar preço (opcional) – RN48.
- Botões "Salvar" (rascunho CSV/JSON) e "Finalizar" (persistência no banco).
- **Apenas itens com quantidade alterada** (diferente da quantidade em estoque antes da atualização) devem ser atualizados no banco (RN51).
- Itens exibidos por categoria e ordem alfabética (RN52).

### 5.1. Tratamento de data duplicada (RN60)

Ao registrar uma compra, se já existir uma ou mais compras cadastradas para a mesma data informada:

1. O sistema deve exibir a mensagem:  
   *"Já existe(m) compra(s) registrada(s) para a data [data]. Deseja somar os itens a uma compra existente ou criar um novo registro de compra?"*

2. Oferecer as opções: **"Somar"** e **"Novo cadastro"**.

3. Se o usuário escolher **"Novo cadastro"**:
   - Inserir um novo registro na tabela `compra` (com a mesma data) e seus respectivos itens em `item_compra`.

4. Se o usuário escolher **"Somar"**:
   - **Caso único**: Se existir apenas uma compra naquela data, somar automaticamente as quantidades dos itens da nova compra aos itens da compra existente (atualizando `item_compra.quantidade`).
   - **Múltiplas compras**: Se existir mais de uma compra na data, exibir uma lista (ex: ID da compra, valor total, data) para o usuário escolher a qual compra deseja somar.
   - Após a escolha, somar as quantidades dos itens à compra selecionada. Se um item não existir na compra original, adicionar um novo registro em `item_compra`.
   - O usuário pode vincular um item mais de uma vez à mesma compra (permitido, mas a soma deve acumular quantidades).
   - Se o mesmo item for adicionado a mesma compra e possuir o mesmo preço, ele deve ser unido ao item compra já existente. Do contrário, deve ser registrado um novo item_compra.

**Regras de negócio relacionadas:** RN46 a RN53.

---

## 6. Tabela de Itens Comprados (RN53)

Deve armazenar: identificador do item, data da compra, quantidade comprada, marca, preço.  
(Ver `05-data-rules.md` para detalhes.)