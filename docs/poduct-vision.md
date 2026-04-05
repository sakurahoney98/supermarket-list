# Supermarket List

## 📌 Informações Gerais
- **Projeto:** Supermarket List  
- **Autor:** Caroline  S.
- **Versão:** 0.1  
- **Data:** 04/04/2026  

---

## 🎯 Objetivo
Gerar automaticamente listas de compras com base nas informações fornecidas pelo usuário, além de permitir a geração de relatórios analíticos sobre compras, gastos e consumo.

---

## 💡 Justificativa
A criação manual de listas de compras está sujeita a erros humanos, como esquecimento de itens ou compra em quantidade inadequada.

O sistema automatiza esse processo, garantindo:
- maior precisão
- melhor controle de estoque doméstico
- definição da quantidade ideal de compra

---

## 📦 Escopo

O sistema deverá permitir:

- Gerar lista de compras com quantidade sugerida
- Pré-visualizar a lista antes da geração final
- Gerenciar itens (criar, editar, excluir)
- Gerenciar categorias
- Atualizar estoque manualmente
- Registrar compras realizadas
- Gerar relatórios:
  - Itens comprados por mês
  - Gastos por item
  - Consumo de itens
- Exportar:
  - Lista em PDF
  - Relatórios em Excel

---

## 🚫 Fora do Escopo

O sistema não contemplará:

- Login/autenticação
- Download em formatos diferentes de PDF (lista) e Excel (relatórios)
- Cadastro de marcas de produtos (MVP)
- Relatórios não especificados

---

## ⚙️ Requisitos Funcionais

### 🛒 Lista de Compras
- Gerar pré-visualização da lista
- Permitir ajuste manual das quantidades antes da geração final
- Exportar lista em PDF

### 📦 Itens
- Criar item com:
  - Nome
  - Unidade/peso
  - Quantidade em estoque
  - Quantidade máxima
  - Data da última compra
  - Categoria
- Editar item
- Excluir item
- Visualizar itens (lista ordenada por nome)
- Visualizar detalhes de um item

### 🏷️ Categorias
- Criar categoria
- Editar categoria
- Excluir categoria

### 📊 Estoque
- Atualizar quantidade dos itens manualmente

### 🧾 Compras
- Registrar compra com:
  - Data
  - Marca
  - Preço
  - Quantidade
- Atualizar data da última compra automaticamente

### 📈 Relatórios
- Itens comprados por mês
- Gastos por item (intervalo de tempo)
- Consumo de itens (intervalo de tempo)

---

## 🧩 Requisitos Não Funcionais

- Interface simples e intuitiva
- Design visual agradável (estilo “cute”)
- Aplicação web responsiva
- Não requer autenticação (MVP)

---

## 🛠️ Tecnologias (Proposta Inicial)

- **Backend:** Java + Spring Boot  
- **Frontend:** Angular  
- **Banco de Dados:** PostgreSQL  

---

## 📝 Observações

Este documento representa a visão inicial do sistema e poderá evoluir ao longo do desenvolvimento.