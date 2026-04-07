# Regras de Interface

## 1. Listagem

- Itens devem ser exibidos:
  - Ordenados por categoria
  - Em ordem alfabética

- Exibição básica:
  - ID
  - Nome

---

## 2. Pré-visualização da Lista

- Deve permitir:
  - Aumentar quantidade
  - Diminuir quantidade

- Se a quantidade for ajustada para 0:
  - O item deve ser removido da lista

- Deve conter:
  - Botão para salvar temporariamente (CSV ou JSON)
  - Botão para finalizar e gerar a lista

---

## 3. Atualização de Estoque

- Exibir:
  - Nome
  - Categoria
  - Quantidade

- Permitir:
  - Incrementar/decrementar valores

- Alterações devem ser salvas mediante ação do usuário

---

## 4. Registro de Compras

- Exibir lista de itens com:
  - Nome
  - Categoria
  - Quantidade

- Permitir informar:
  - Marca (opcional)
  - Preço (opcional)
  - Quantidade

- Deve conter:
  - Botão "Salvar" (temporário)
  - Botão "Finalizar" (persistência)

---

## 5. Experiência do Usuário

- Interface simples e intuitiva
- Feedback visual para erros
- Design visual agradável (estilo “cute”)
- Sistema responsivo