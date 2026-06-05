import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CategoriaService } from '../../services/categoria';
import { ItemService } from '../../services/item';

import { PageTitleComponent } from '../../components/page-title/page-title';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { EmptyListComponent } from '../../components/empty-list/empty-list';
import { ToastMessageErrorComponent } from '../../components/toast-message-error/toast-message-error';
import { ToastMessageSucessComponent } from '../../components/toast-message-sucess/toast-message-sucess';

import { ItemModel } from '../../models/item.model';
import { CategoriaModel } from '../../models/categoria.model';
import { AtualizarEstoqueRequestModel } from '../../models/atualizar-estoque-request.model';



@Component({
  selector: 'app-atualizar-estoque',
  imports: [PageTitleComponent, SearchBarComponent, EmptyListComponent, ToastMessageErrorComponent, ToastMessageSucessComponent, CommonModule, FormsModule],
  standalone: true,
  templateUrl: './atualizar-estoque.html',
  styleUrl: './atualizar-estoque.css',
})
export class AtualizarEstoque implements OnInit {
  listaItens: ItemModel[] = [];
  listaItensCopy: ItemModel[] = [];
  listaCategoriasExibicao: CategoriaModel[] = [];
  listaCategoriasFiltro: CategoriaModel[] = []


  exibirFiltro: boolean = false;
  estoqueModificado: boolean = false;
  exibirToastSucesso: boolean = false;
  exibirToastErro: boolean = false;

  emojiListaVazia: string = '📭';
  tituloListaVazia: string = 'Nenhum item encontrado';
  descricaoListaVazia: string = 'Crie um item para atualizar seu estoque.';
  termoBuscado: string = '';
  mensagemToast: string = '';

  categoriaFiltro: CategoriaModel = {
    id: 0,
    nome: 'Todas categorias',
    ativo: true,
    corLetra: '',
    corFundo: '',
    selecionado: false
  };
  categoriaSelecionada: CategoriaModel = this.categoriaFiltro;

  constructor(
    private itemService: ItemService,
    private categoriaService: CategoriaService) { }

  ngOnInit() {
    this.iniciarVariaveis();
    this.carregarDados();


  }

  private iniciarVariaveis() {
    this.listaItens = [];
    this.listaItensCopy = [];
    this.listaCategoriasExibicao = [];

    this.listaCategoriasFiltro = [this.categoriaFiltro];

    this.termoBuscado = '';
    this.categoriaSelecionada = this.categoriaFiltro;
  }

  private carregarDados() {
    this.capturarCategorias();
    this.capturarItens();
    this.estoqueModificado = this.isAlteracaoDetectada();
  }


  capturarItens() {
    let rascunho: ItemModel[] = JSON.parse(localStorage.getItem("listaAtualizarEstoque") || "[]");


    this.itemService.getItensEstoque().subscribe({
      next: (data: ItemModel[]) => {

        this.listaItens = data;

        this.listaItens = data.map(c => ({
          ...c,
          novoValor: c.quantidadeEstoque
        }));

        if (rascunho.length > 0) {
          this.listaItens.forEach(item => {
            const itemRascunho = rascunho.find(i => i.id === item.id);

            if (itemRascunho) {
              item.novoValor = itemRascunho.novoValor;
            }

          })



        }

        this.listaItens.forEach(item => {
          const categoriaItem = item.categoria

          if (this.listaCategoriasExibicao.filter(c => c.id === categoriaItem.id).length === 0) {
            this.listaCategoriasExibicao.push(categoriaItem);
          }
        })

        this.listaItensCopy = this.listaItens;

        this.estoqueModificado = this.isAlteracaoDetectada();




      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  capturarCategorias() {

    this.categoriaService.getCategorias().subscribe({
      next: (data: CategoriaModel[]) => {
        this.listaCategoriasFiltro.push(...data);

      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  onBusca(termo?: string) {




    if (termo !== undefined) {
      this.termoBuscado = termo;
    }

    if (this.categoriaSelecionada.id !== 0 && this.termoBuscado !== '') {

      this.listaItensCopy = this.listaItens.filter(i => i.categoria.id === this.categoriaSelecionada.id && i.nome.toLowerCase().includes(this.termoBuscado.toLowerCase()));

    } else if (this.categoriaSelecionada.id !== 0) {

      this.listaItensCopy = this.listaItens.filter(i => i.categoria.id === this.categoriaSelecionada.id);

    } else if (this.termoBuscado !== '') {

      this.listaItensCopy = this.listaItens.filter(i => i.nome.toLowerCase().includes(this.termoBuscado.toLowerCase()));

    } else {
      this.listaItensCopy = this.listaItens;
    }

    this.listaCategoriasExibicao = [];
    this.listaItensCopy.forEach(item => {
      const categoriaItem = item.categoria

      if (this.listaCategoriasExibicao.filter(c => c.id === categoriaItem.id).length === 0) {
        this.listaCategoriasExibicao.push(categoriaItem);
      }
    })

  }

  salvarRascunho() {
    let rascunhoRecuperado: ItemModel[] = JSON.parse(localStorage.getItem("listaAtualizarEstoque") || "[]");

    if (rascunhoRecuperado.length === 0) {
      localStorage.setItem('listaAtualizarEstoque', JSON.stringify(this.listaItens));

    } else {

      let mapaRascunho = new Map(rascunhoRecuperado.map(item => [item.id, item]));


      this.listaItens.forEach(itemAtual => {
        if (itemAtual.quantidadeEstoque !== itemAtual.novoValor) {

          mapaRascunho.set(itemAtual.id, { ...itemAtual });
        }
      });

      const rascunhoAtualizado = Array.from(mapaRascunho.values());

      localStorage.setItem('listaAtualizarEstoque', JSON.stringify(rascunhoAtualizado));

    }


    this.capturarItens();
    this.exibirMensagemDeSucesso("Rascunho salvo com sucesso!");

    


  }

  salvarAlteracoes() {
    const itensAlterados = this.listaItens.filter(i => i.quantidadeEstoque !== i.novoValor);
    let listaAtualizarEstoque: AtualizarEstoqueRequestModel[] = [];

    itensAlterados.forEach(item => {
      const objeto: AtualizarEstoqueRequestModel = {
        ideItem: item.id,
        quantidadeAtual: item.quantidadeEstoque,
        quantidadeNova: item.novoValor
      }

      listaAtualizarEstoque.push(objeto);

    })

    this.itemService.atualizarEstoque(listaAtualizarEstoque).subscribe({
      next: () => {

        this.exibirMensagemDeSucesso("Itens atualizados com sucesso!")


        this.iniciarVariaveis();
        this.carregarDados();
        this.estoqueModificado = this.isAlteracaoDetectada();

      },
      error: (err) => {

        this.exibirMensagemDeErro(err.error)

      }
    })


  }

  toggleSelecionarCategoriaFiltro(categoria: CategoriaModel) {

    this.categoriaSelecionada = categoria;
    this.exibirFiltro = false;

    this.onBusca();

  }

  isAlteracaoDetectada() {
    return this.listaItens.filter(i => i.quantidadeEstoque !== i.novoValor).length > 0;
  }

  aumentarValor(item: ItemModel) {
    item.novoValor = Number(item.novoValor) + 1;
    const itemListaPrincipal = this.listaItens.filter(i => i.id === item.id)[0];
    itemListaPrincipal.novoValor = item.novoValor;
    this.estoqueModificado = this.isAlteracaoDetectada();
  }

  diminuirValor(item: ItemModel) {
    if (item.novoValor > 0) {
      item.novoValor = Number(item.novoValor) - 1;
      const itemListaPrincipal = this.listaItens.filter(i => i.id === item.id)[0];
      itemListaPrincipal.novoValor = item.novoValor;
      this.estoqueModificado = this.isAlteracaoDetectada();
    }
  }

  resetarValor(item: ItemModel) {
    item.novoValor = Number(item.quantidadeEstoque);
    const itemListaPrincipal = this.listaItens.filter(i => i.id === item.id)[0];
    itemListaPrincipal.novoValor = item.novoValor;
    this.estoqueModificado = this.isAlteracaoDetectada();

  }

  resetarTudo() {
    this.listaItens = this.listaItens.map(c => ({
      ...c,
      novoValor: c.quantidadeEstoque
    }));

    this.listaItensCopy = this.listaItens.map(c => ({
      ...c,
      novoValor: c.quantidadeEstoque
    }));
    this.estoqueModificado = this.isAlteracaoDetectada();
    localStorage.removeItem("listaAtualizarEstoque");


  }

  exibirMensagemDeSucesso(mensagem: string) {
    this.mensagemToast = mensagem;

    this.exibirToastSucesso = true;
    setTimeout(() => {
      this.exibirToastSucesso = false;
    }, 5000);
  }

  exibirMensagemDeErro(mensagem: string) {
    this.mensagemToast = mensagem;

    this.exibirToastErro = true;
    setTimeout(() => {
      this.exibirToastErro = false;
    }, 5000);
  }


}
