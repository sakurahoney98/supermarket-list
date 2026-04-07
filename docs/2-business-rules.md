# Regras de Negócio

## 1. Cálculo de Sugestão de Compra

A sugestão de compra de um item é determinada com base na sua duração e nas quantidades em estoque e máxima.

### 1.1. Regra Geral Preliminar
- Se a quantidade em estoque for **maior ou igual** à quantidade máxima de compra, o item **não deve ser incluído** na lista de compras.

### 1.2. Itens com duração ≥ 30 dias
- Se a quantidade em estoque for **igual a zero**:
  - A quantidade de compra sugerida será a **quantidade máxima de compra**.
- Caso contrário (estoque > 0):
  - O item **não deve ser comprado** (quantidade sugerida = 0).

### 1.3. Itens com duração < 30 dias
A quantidade sugerida é calculada em duas etapas:

1. **Cálculo por reposição imediata**  
   `necessidade_imediata = quantidade_maxima - quantidade_estoque`

2. **Cálculo por consumo mensal estimado**  
   `necessidade_mensal = ceil(30 / duracao) - quantidade_estoque`  
   (onde `ceil` é o arredondamento para cima)

3. **Quantidade final sugerida**  
   `quantidade_sugerida = min(necessidade_imediata, necessidade_mensal)`  
   Se o resultado for negativo, a quantidade sugerida deve ser **0**.

> **Exemplo:**  
> Item com duração = 7 dias, estoque = 2, máximo = 10.  
> Necessidade imediata = 10 - 2 = 8.  
> Necessidade mensal = ceil(30/7)=5 → 5 - 2 = 3.  
> Quantidade sugerida = min(8,3) = **3 unidades**.

---

## 2. Geração da Lista de Compras

A lista final de compras deve obedecer às seguintes regras:

- A lista deve ser **agrupada por categoria** e **ordenada alfabeticamente** pelo nome do item dentro de cada categoria.
- Cada item deve exibir: **nome** e **quantidade sugerida**.
- Itens cuja quantidade sugerida for **zero** não devem aparecer na lista.
- A lista deve ser **exportada em formato PDF** com a mesma estrutura (categorias e ordem alfabética).

---

## 3. Exclusão Automática de Itens com Quantidade Zero (Pré-visualização)

Na tela de pré-visualização da lista, se o usuário ajustar a quantidade de um item para **0** (usando os botões de aumentar/diminuir), o item deve ser **removido imediatamente** da lista exibida.

---

## 4. Persistência Seletiva de Alterações

- Na tela de edição de estoque e na tela pós-compra, apenas os itens cuja **quantidade foi alterada** (ou que tiveram marca/preço informados) devem ser atualizados no banco de dados.
- Itens sem alteração não geram operação de escrita.

---

## 5. Exclusão Lógica (Soft Delete)

- Tanto itens quanto categorias devem ser **excluídos logicamente**: um campo indicador (ex: `ativo` ou `deletado_em`) deve ser usado para ocultar o registro, mas mantê-lo no banco para histórico.
- Listagens e relatórios devem considerar apenas registros **não excluídos logicamente**, a menos que explicitamente especificado.

---

## 6. Bloqueio de Exclusão de Categoria com Itens Vinculados

Não é possível excluir (nem lógica, nem fisicamente) uma categoria que esteja vinculada a qualquer item. O sistema deve exibir uma mensagem informativa e impedir a ação.

---

## 7. Regra de Inclusão Automática para Estoque Zero

Se um item estiver com **quantidade em estoque igual a zero**, ele deve ser **automaticamente incluído na lista de compras** (seguindo o cálculo de sugestão da seção 1).