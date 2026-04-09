# Regras de Validação

## 1. Cadastro de Itens

### 1.1. Campos obrigatórios (RN17)
- Nome
- Quantidade em estoque
- Quantidade máxima de compra (agora obrigatório – mudou!)
- Categoria
- Duração da unidade do item (em dias)

### 1.2. Campos opcionais
- Peso ou unidade
- Data da última compra

### 1.3. Validações específicas
- **RN18/RN19:** Antes de permitir cadastro de item, deve existir pelo menos uma categoria cadastrada. Caso contrário, bloquear e exibir mensagem.
- **RN20:** Não é permitido cadastrar dois itens com o mesmo nome dentro da mesma categoria.
- **RN21:** Campos obrigatórios não podem ficar em branco.
- **RN22:** Se campo obrigatório estiver vazio, exibir indicativo visual de erro.
- **RN23:** Duração deve ser **maior que 0** (positivo).

---

## 2. Edição de Itens (RN24, RN25)

- O usuário pode editar todos os campos do item.
- Campos obrigatórios continuam obrigatórios (não podem ficar em branco).
- ID do item não é editável.

---

## 3. Exclusão de Itens

- **RN26:** Permitir exclusão em massa de itens (seleção múltipla).
- **RN27:** Exclusão lógica (soft delete).
- A exclusão em massa deve seguir as mesmas regras de soft delete e confirmação.

---

## 4. Cadastro de Categorias

### 4.1. Campos obrigatórios (RN33)
- Nome
- Cor da letra
- Cor de fundo

### 4.2. Validações
- **RN34:** Exibir pré-visualização em tempo real das cores durante o cadastro.
- **RN35:** Não permitir cadastrar categorias com nomes duplicados.
- Campos obrigatórios não podem ficar em branco (aplicar RN21/RN22).

---

## 5. Exclusão de Categorias

- **RN37:** Permitir exclusão em massa de categorias.
- **RN38:** Exclusão lógica.
- **RN39/RN40:** Antes de excluir (individual ou em massa), verificar se a categoria está vinculada a algum item. Se estiver, **bloquear a exclusão** e informar o usuário.

---

## 6. Regras Gerais de Validação (reutilizáveis)

- Todos os campos obrigatórios (itens e categorias) seguem RN21/RN22.
- Nomes duplicados: item dentro da mesma categoria (RN20); categoria globalmente (RN35).
- Feedback visual claro para erros.