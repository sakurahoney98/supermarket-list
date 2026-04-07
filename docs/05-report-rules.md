# Regras de Relatórios

## 1. Relatório de Itens Comprados por Mês

**Parâmetros obrigatórios:**
- Mês (1 a 12)
- Ano

**Conteúdo do relatório:**
- Lista de itens comprados no período, com:
  - Nome do item
  - Quantidade total comprada
  - Marca (se informada)
  - Preço total gasto (soma dos preços registrados)

**Formato:** PDF.

---

## 2. Relatório de Valores Gastos por Item

**Parâmetros obrigatórios:**
- Item (selecionado a partir da lista de itens ativos)
- Intervalo de tempo (data inicial e data final)

**Conteúdo:**
- Nome do item
- Período selecionado
- Total gasto
- Quantidade total comprada no período
- Preço médio por unidade

**Formato:** PDF.

---

## 3. Relatório de Consumo de Itens

**Parâmetros obrigatórios:**
- Um ou mais itens (seleção múltipla)
- Intervalo de tempo (data inicial e data final)

**Cálculo do consumo:**

`consumo = (estoque_inicial + quantidade_comprada_no_periodo) - estoque_final`

Onde:
- `estoque_inicial` = estoque no dia anterior à data inicial
- `estoque_final` = estoque no dia seguinte à data final (ou atual, se final = hoje)

**Conteúdo do relatório:**
- Nome do item
- Quantidade comprada no período
- Consumo estimado
- Saldo inicial e final

**Formato:** PDF.

> Nota: O sistema deve basear-se nas atualizações de estoque registradas pela tela pós-compra para calcular o consumo.