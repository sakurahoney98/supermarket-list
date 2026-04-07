# Regras de Negócio

## 1. Regras de Cálculo de Compra

### 1.1 Regra Geral
- Se a quantidade em estoque for maior ou igual à quantidade máxima de compra, o item não deve ser incluído na lista de compras.

### 1.2 Itens de Longa Duração (≥ 30 dias)
- Se a quantidade em estoque for igual a 0:
  - Comprar a quantidade máxima definida.
- Caso contrário:
  - Não comprar o item.

### 1.3 Demais Itens (< 30 dias)

A quantidade de compra deve ser calculada da seguinte forma:

1. Calcular:
`quantidade_maxima - quantidade_estoque`

2. Calcular a necessidade mensal:
`ceil(30 / duração)`

3. Subtrair a quantidade em estoque do valor obtido no passo anterior.

4. Comparar os dois valores:
- Resultado do passo 1
- Resultado do passo 3

5. A quantidade final de compra deve ser o menor valor entre os dois.

---

## 2. Regras da Lista de Compras

- A lista deve ser:
  - Agrupada por categoria
  - Ordenada alfabeticamente

- Cada item deve conter:
  - Nome
  - Quantidade sugerida

- Itens com quantidade em estoque igual a 0 devem ser incluídos automaticamente na lista.

- Itens com quantidade ajustada para 0 devem ser removidos da lista.

- A lista final deve ser exportada em PDF.