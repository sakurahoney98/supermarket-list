# Regras de Interface

## 1. Listagem de Itens Cadastrados
- Exibir: **ID**, **Nome** e **Categoria** de cada item.
- Ordenação: **alfabética** pelo nome do item.
- Funcionalidades obrigatórias na tela:
  - **Busca por nome** (filtro textual, case insensitive).
  - **Filtro por categoria** (dropdown com todas as categorias ativas).
  - Possibilidade de combinar busca + filtro.
  - Botões para **editar**, **excluir** (ou selecionar para exclusão em massa) e **visualizar** detalhes.

## 2. Visualização de Detalhes de um Item (somente leitura)
Ao clicar em "Visualizar", o sistema exibe (sem edição):
- ID, Nome, Peso/unidade, Quantidade em estoque, Quantidade máxima de compra, Data da última compra, Categoria, Duração.
- Botão "Fechar" para retornar à lista.

## 3. Pré-visualização da Lista de Compras
- Exibe todos os itens que **entrariam na lista** (conforme regras de negócio), agrupados por categoria e em ordem alfabética.
- Para cada item: nome, quantidade sugerida, botões **+** / **-** para ajustar quantidade, e um botão **Resetar** (retorna à quantidade sugerida original).
- Se a quantidade for ajustada para 0, o item some da lista (exclusão visual imediata).
- Dois botões principais:
  - **Salvar rascunho** → exporta o estado atual da lista em CSV ou JSON.
  - **Finalizar** → gera o PDF final da lista (conforme regras de negócio).

## 4. Tela de Edição de Estoque (pré-compra)
- Lista todos os itens ativos, com: nome, categoria, quantidade atual (editável via botões +/- e reset).
- Ordenação: por categoria + alfabética.
- Botão **Salvar** → persiste as alterações feitas no banco (apenas itens modificados).

## 5. Tela de Atualização Pós-Compra (após ir ao mercado)
- Permite registrar o que foi efetivamente comprado.
- Exibe: nome, categoria, quantidade (com botões +/- e reset).
- Campos opcionais por item: **marca** (texto) e **preço** (número decimal).
- Botões:
  - **Salvar** → exporta dados em CSV/JSON (rascunho).
  - **Finalizar** → persiste no banco (atualiza estoque e registra compra na tabela de histórico).
- Lista ordenada por categoria + alfabética.

## 6. Listagem de Categorias
- Exibe: ID e Nome de cada categoria ativa.
- Ordenação alfabética.
- Permite: editar, excluir (individual ou em massa) e visualizar.

## 7. Feedback Visual e Experiência do Usuário
- O sistema deve ser **responsivo** (funciona em desktop e mobile).
- Estilo visual: **simples, limpo e agradável** (sugestão: "cute", com cores suaves e bordas arredondadas).
- Erros de validação (campos obrigatórios, exclusão bloqueada, etc.) devem ser exibidos com mensagens claras e destaque visual.
- Todas as ações destrutivas (exclusão, reset em massa) devem pedir confirmação.