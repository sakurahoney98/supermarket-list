# Regras de Relatórios

## 1. Relatório de Itens Comprados por Mês (RN54)

**Parâmetros:** mês, ano.

**Conteúdo (por item comprado no período):**
- Nome do item
- Marca
- Preço
- Quantidade comprada

**Formato de exportação:** XLSX (RN59).

---

## 2. Relatório de Gastos por Item (RN55)

**Parâmetros:** intervalo de tempo (data inicial e final), item específico.

**Conteúdo:**
- Data da compra
- Preço
- Valor total pago (pode ser preço × quantidade, se o preço registrado for unitário – definir consistência)

**Formato:** XLSX.

---

## 3. Relatório de Consumo de Itens (RN56, RN57)

**Parâmetros:** item, intervalo de tempo.

**Conteúdo:**
- Data de compra
- Quantidade comprada
- **Diferença em dias entre a compra atual e a compra imediatamente anterior do mesmo item** (RN57)

**Observação:** A diferença de dias ajuda a calcular a frequência de compra e o ritmo de consumo.

**Formato:** XLSX.

---

## 4. Regra Geral de Relatórios

- Todos os relatórios devem ser exportados no formato **XLSX** (RN59).
- Itens/categorias excluídos logicamente não devem aparecer nos relatórios, a menos que explicitamente necessário (RN58).