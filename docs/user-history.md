## US01 - Cadastrar categoria

**Como** usuário  
**Quero** cadastrar uma categoria  
**Para** organizar meus itens de compra  

**Critérios de aceite:**  
- O formulário deve conter os campos: Nome (obrigatório), Cor da letra (obrigatório), Cor de fundo (obrigatório) – conforme RN33  
- O sistema deve exibir uma pré-visualização em tempo real com as cores escolhidas (RN34)  
- Não é permitido cadastrar duas categorias com o mesmo nome (RN35)  
- Campos obrigatórios não podem ficar em branco; caso estejam, exibir indicativo visual de erro (RN21, RN22)  
- Após salvar, a categoria deve aparecer na listagem de categorias  

**Regras de negócio relacionadas:** RN33, RN34, RN35, RN21, RN22 (conforme `02-validation-rules.md` e `03-interface-rules.md`)


---

## US02 - Excluir categoria

**Como** usuário  
**Quero** excluir uma categoria  
**Para** remover categorias que não utilizo mais  

**Critérios de aceite:**  
- O usuário pode excluir uma categoria individualmente a partir da listagem (ex: botão "Excluir")  
- Antes de excluir, o sistema deve verificar se a categoria está vinculada a algum item (RN39)  
- Se houver itens vinculados, a exclusão deve ser **bloqueada** e exibir mensagem informativa: *"Não é possível excluir a categoria [nome] pois ela está vinculada a [X] itens."* (RN40)  
- Se não houver itens vinculados, a exclusão deve ser **lógica** (soft delete) – a categoria não aparece mais nas listagens padrão, mas permanece no banco (RN38, RN58)  
- O sistema deve pedir confirmação antes de excluir: *"Tem certeza que deseja excluir a categoria [nome]?"*  

**Regras de negócio relacionadas:** RN37, RN38, RN39, RN40, RN58 (conforme `02-validation-rules.md` e `05-data-rules.md`)

---

## US03 - Visualizar categorias

**Como** usuário  
**Quero** visualizar minhas categorias  
**Para** consultar e gerenciar minha organização  

**Critérios de aceite:**  
- O sistema deve exibir uma tela com a lista de todas as categorias **ativas** (não excluídas logicamente) – RN36, RN58  
- Cada linha da lista deve mostrar: **ID** e **Nome** da categoria (aplicando as cores definidas – letra e fundo) – RN36  
- A lista deve ser ordenada **alfabeticamente** pelo nome da categoria  
- A tela deve permitir ações por categoria: **Excluir** individual.
- Deve haver suporte para **exclusão em massa** (selecionar múltiplas categorias e excluir de uma vez), respeitando a regra de bloqueio para categorias vinculadas a itens (nenhuma das selecionadas pode ter itens) – RN37, RN39, RN40  
- Campo de busca por nome para filtrar categorias  

**Regras de negócio relacionadas:** RN36, RN37, RN38, RN39, RN40, RN58 (conforme `03-interface-rules.md` e `02-validation-rules.md`)


## US04 - Cadastrar item

**Como** usuário  
**Quero** cadastrar um item  
**Para** incluí-lo no controle de compras  

**Critérios de aceite:**  
- O formulário deve conter os campos obrigatórios: Nome, Quantidade em estoque, Quantidade máxima de compra, Categoria, Duração (em dias) – conforme RN17  
- Campos opcionais: Peso ou unidade, Data da última compra – RN17  
- Antes de permitir o cadastro, o sistema deve verificar se existe pelo menos uma categoria cadastrada (RN18). Caso não exista, bloquear e exibir mensagem (RN19)  
- Não é permitido cadastrar dois itens com o mesmo nome dentro da mesma categoria (RN20)  
- Duração deve ser maior que 0 (RN23)  
- Campos obrigatórios não podem ficar em branco; caso estejam, exibir indicativo visual de erro (RN21, RN22)  
- Após salvar, o item deve aparecer na listagem de itens  

**Regras de negócio relacionadas:** RN17, RN18, RN19, RN20, RN21, RN22, RN23 (conforme `02-validation-rules.md` e `03-interface-rules.md`)

---

## US05 - Editar item

**Como** usuário  
**Quero** editar um item  
**Para** atualizar suas informações  

**Critérios de aceite:**  
- O usuário pode acessar a edição a partir da listagem de itens (ex: botão "Editar")  
- O formulário de edição deve ser pré-preenchido com os dados atuais do item  
- Todos os campos do item podem ser editados (RN24)  
- Campos obrigatórios continuam obrigatórios e não podem ficar em branco (RN25, RN21, RN22)  
- Não é permitido alterar o nome do item para outro que já exista na mesma categoria (RN20)  
- A duração deve permanecer maior que 0 (RN23)  
- O ID do item não é editável  
- Após salvar, as alterações devem ser refletidas na listagem e em todos os lugares onde o item é exibido  

**Regras de negócio relacionadas:** RN24, RN25, RN20, RN23, RN21, RN22 (conforme `02-validation-rules.md`)

---

## US06 - Excluir item

**Como** usuário  
**Quero** excluir um item  
**Para** removê-lo do sistema  

**Critérios de aceite:**  
- O usuário pode excluir um item individualmente a partir da listagem (ex: botão "Excluir")  
- O sistema deve permitir também exclusão em massa de itens (seleção múltipla) – RN26  
- A exclusão deve ser **lógica** (soft delete) – RN27  
- Itens excluídos logicamente não devem aparecer nas listagens padrão (RN58)  
- O sistema deve pedir confirmação antes de excluir (individual ou em massa)  


**Regras de negócio relacionadas:** RN26, RN27, RN58 (conforme `02-validation-rules.md` e `05-data-rules.md`)

---

## US07 - Visualizar itens

**Como** usuário  
**Quero** visualizar meus itens  
**Para** acompanhar o que está cadastrado  

**Critérios de aceite:**  
- O sistema deve exibir uma lista com todos os itens **ativos** (não excluídos logicamente) – RN28, RN58  
- Cada linha da lista deve mostrar: **ID**, **Nome** e **Categoria** do item – RN28  
- A lista deve ser ordenada **alfabeticamente** pelo nome do item – RN28  
- O sistema deve permitir **busca por nome** (filtro textual, não case insensitive) – RN29  
- O sistema deve permitir **filtro por categoria** (dropdown com categorias ativas) – RN30  
- É possível combinar busca + filtro simultaneamente  
- Ao clicar em um item, o sistema deve exibir uma tela de **visualização detalhada** (somente leitura) com todos os campos: ID, nome, peso/unidade, quantidade em estoque, quantidade máxima de compra, data da última compra, categoria, duração – RN31, RN32  

**Regras de negócio relacionadas:** RN28, RN29, RN30, RN31, RN32, RN58 (conforme `03-interface-rules.md`)

---

## US08 - Atualizar estoque

**Como** usuário  
**Quero** atualizar o estoque de um item  
**Para** manter as quantidades corretas  

**Critérios de aceite:**  
- O sistema deve exibir uma tela com a lista de todos os itens cadastrados (ativos), mostrando: **Nome**, **Categoria** e **Quantidade em estoque** – RN41  
- Para cada item, devem haver botões para **aumentar**, **diminuir** e **resetar** a quantidade (reset retorna ao valor inicial antes das alterações) – RN42  
- A listagem deve ser organizada por **categoria** e, dentro de cada categoria, em **ordem alfabética** – RN45  
- A tela deve conter um botão **"Salvar"** que armazena as alterações temporariamente em formato CSV ou JSON – RN43  
- A tela deve conter um botão **"Finalizar"** que persiste as alterações no banco de dados – RN44  
- **Apenas itens cuja quantidade foi alterada** devem ser atualizados no banco (persistência seletiva) – RN51 
- O usuário deve ser informado do sucesso da operação  

**Regras de negócio relacionadas:** RN41, RN42, RN43, RN44, RN45, RN51 (conforme `01-business-rules.md` e `03-interface-rules.md`)

---

## US09 - Gerar lista de compras

**Como** usuário  
**Quero** gerar uma lista de compras  
**Para** saber o que preciso comprar  

**Critérios de aceite:**  
- O sistema deve calcular a quantidade sugerida para cada item com base na duração, estoque e quantidade máxima (RN10 a RN15)  
- Itens com quantidade em estoque maior ou igual à quantidade máxima de compra não devem ser incluídos (RN16)  
- Itens com estoque igual a 0 devem ser automaticamente incluídos (RN9)  
- A lista gerada deve conter apenas itens com quantidade sugerida > 0 (RN5)  
- A lista deve ser agrupada por categoria e ordenada alfabeticamente (RN1, RN8)  

**Regras de negócio relacionadas:** RN1, RN5, RN8, RN9, RN10, RN11, RN12, RN13, RN14, RN15, RN16 (conforme `01-business-rules.md`)

---

## US10 - Visualizar prévia da lista

**Como** usuário  
**Quero** visualizar a lista antes de finalizar  
**Para** revisar os itens sugeridos  

**Critérios de aceite:**  
- A tela de pré-visualização deve exibir todos os itens cadastrados, separados por categoria e em ordem alfabética (RN1)  
- Cada item deve mostrar: nome, quantidade a ser comprada (sugerida), quantidade em estoque, botões +, - e reset (RN2, RN3)  
- O usuário pode escolher entre visualizar "todos os itens" ou "apenas os itens que serão incluídos na lista final" (RN4)  
- Itens com quantidade sugerida 0 não aparecem na lista final (RN5), mas podem aparecer se o usuário escolher ver todos  
- A prévia deve ser atualizada em tempo real conforme o usuário ajusta as quantidades  

**Regras de negócio relacionadas:** RN1, RN2, RN3, RN4, RN5 (conforme `01-business-rules.md` e `03-interface-rules.md`)

---

## US11 - Ajustar lista de compras

**Como** usuário  
**Quero** ajustar quantidades da lista  
**Para** adaptar às minhas necessidades  

**Critérios de aceite:**  
- Na tela de pré-visualização, cada item deve ter botões para aumentar (+), diminuir (-) e resetar a quantidade (RN2, RN3)  
- Resetar retorna a quantidade ao valor inicial sugerido pelo sistema (RN3)  
- Se o usuário ajustar a quantidade de um item para 0, esse item deve ser removido da lista final (RN5)  
- As alterações feitas pelo usuário devem ser refletidas imediatamente na visualização da lista  
- O sistema deve permitir salvar as alterações temporariamente (botão "Salvar" em CSV/JSON – RN6)  

**Regras de negócio relacionadas:** RN2, RN3, RN5, RN6 (conforme `01-business-rules.md` e `03-interface-rules.md`)

---

## US12 - Exportar lista

**Como** usuário  
**Quero** exportar a lista em PDF  
**Para** utilizá-la fora do sistema  

**Critérios de aceite:**  
- Na tela de pré-visualização, deve existir um botão **"Finalizar"** que confirma as alterações e realiza o download da lista (RN7)  
- A lista exportada deve estar em formato **PDF** (RN8)  
- O PDF deve conter os itens: separados por categoria, ordenados alfabeticamente, com nome e quantidade a ser comprada (RN8)  
- Apenas os itens com quantidade > 0 (após os ajustes do usuário) devem constar no PDF  
- O nome do arquivo PDF deve ser sugestivo (ex: `lista_compras_YYYYMMDD.pdf`)  

**Regras de negócio relacionadas:** RN7, RN8 (conforme `01-business-rules.md`)

---

## US13 - Registrar compra

**Como** usuário  
**Quero** registrar uma compra realizada  
**Para** atualizar meu estoque  

**Critérios de aceite:**  
- O sistema deve disponibilizar uma tela para atualização após a compra (RN46)  
- A tela deve conter: identificação da data de ida ao supermercado (obrigatória) (RN47a)  
- Lista de todos os itens cadastrados (ativos) com: nome, categoria e quantidade atual em estoque (RN47b)  
- Para cada item, o usuário pode: alterar a quantidade (comprada), resetar a quantidade, informar marca (opcional) e preço (opcional) (RN48)  
- Os itens devem ser exibidos por categoria e em ordem alfabética (RN52)  
- Deve existir um botão **"Finalizar"** que persiste as informações no banco de dados (RN50)  
- **Apenas itens cuja quantidade foi alterada** (quantidade comprada diferente da quantidade em estoque anterior) devem ser atualizados no banco (RN51)  
- Após finalizar, o sistema deve:  
  - Somar o estoque dos itens pela quantidade comprada  
  - Registrar a compra na tabela de itens comprados (RN53)  
  - Exibir mensagem de sucesso  

**Regras de negócio relacionadas:** RN46, RN47, RN48, RN50, RN51, RN52, RN53 (conforme `01-business-rules.md` e `05-data-rules.md`)

---

## US14 - Salvar rascunho de compra

**Como** usuário  
**Quero** salvar uma compra parcialmente  
**Para** finalizá-la depois  

**Critérios de aceite:**  
- Na mesma tela de atualização após compra (US14), deve existir um botão **"Salvar"** (RN49)  
- Ao clicar em "Salvar", o sistema deve exportar o estado atual da tela (itens, quantidades ajustadas, marcas, preços, data da compra) para um arquivo no formato **CSV ou JSON** (RN49)  
- O arquivo salvo deve permitir que o usuário **retome a edição posteriormente** (carregar o rascunho de volta)  
- O rascunho não altera o banco de dados nem o estoque – é apenas armazenamento temporário externo  
- O sistema deve informar ao usuário onde o arquivo foi salvo e sugerir um nome padrão (ex: `rascunho_compra_YYYYMMDD_HHMMSS.json`)  

**Regras de negócio relacionadas:** RN49 (conforme `01-business-rules.md` e `03-interface-rules.md`)

---

## US15 - Gerar relatório de compras

**Como** usuário  
**Quero** gerar relatório de compras  
**Para** analisar o que comprei  

**Critérios de aceite:**  
- O sistema deve permitir gerar um relatório de itens comprados em um determinado mês e ano (RN54)  
- Parâmetros obrigatórios: mês (1 a 12) e ano (ex: 2026)  
- O relatório deve conter, para cada item comprado no período:  
  - Nome do item  
  - Marca (se informada)  
  - Preço  
  - Quantidade comprada (RN54)  
- O relatório deve ser exportado no formato **XLSX** (RN59)  
- Itens excluídos logicamente não devem aparecer no relatório (RN58)  

**Regras de negócio relacionadas:** RN54, RN58, RN59 (conforme `04-report-rules.md`)

---

## US16 - Gerar relatório de gastos

**Como** usuário  
**Quero** gerar relatório de gastos  
**Para** controlar meus custos  

**Critérios de aceite:**  
- O sistema deve permitir gerar um relatório de gastos para um item específico (RN55)  
- Parâmetros obrigatórios: item (selecionado da lista de itens ativos) e intervalo de tempo (data inicial e data final)  
- O relatório deve conter:  
  - Data da compra  
  - Preço  
  - Valor total pago (preço × quantidade, conforme regra de negócio – RN55)  
- O relatório deve ser exportado no formato **XLSX** (RN59)  
- Itens excluídos logicamente não devem aparecer (RN58)  

**Regras de negócio relacionadas:** RN55, RN58, RN59 (conforme `04-report-rules.md`)

---

## US17 - Gerar relatório de consumo

**Como** usuário  
**Quero** gerar relatório de consumo  
**Para** entender meu uso de itens  

**Critérios de aceite:**  
- O sistema deve permitir gerar um relatório de consumo para um item específico (RN56)  
- Parâmetros obrigatórios: item (selecionado da lista de itens ativos) e intervalo de tempo (data inicial e data final)  
- O relatório deve conter, para cada compra do item no período:  
  - Data da compra  
  - Quantidade comprada  
  - Diferença em dias entre a compra atual e a compra imediatamente anterior do mesmo item (RN57)  
- O relatório deve ser exportado no formato **XLSX** (RN59)  
- Itens excluídos logicamente não devem aparecer (RN58)  

**Regras de negócio relacionadas:** RN56, RN57, RN58, RN59 (conforme `04-report-rules.md`)