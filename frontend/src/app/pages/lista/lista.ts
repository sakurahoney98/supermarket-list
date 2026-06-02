import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ListaService } from '../../services/lista';
import { CategoriaService } from '../../services/categoria';

import { PageTitleComponent } from '../../components/page-title/page-title';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { EmptyListComponent } from '../../components/empty-list/empty-list';

import { CategoriaModel } from '../../models/categoria.model';
import { ItemListaCompraRequest } from '../../models/item-lista-compra-request.model';
import { ItemListaCompraResponse } from '../../models/item-lista-compra-response.model';


@Component({
  selector: 'app-lista',
  standalone: true,
  imports: [PageTitleComponent, SearchBarComponent, EmptyListComponent, CommonModule, FormsModule],
  templateUrl: './lista.html',
  styleUrl: './lista.css',
})
export class Lista implements OnInit {

  listaItensCompra: ItemListaCompraRequest[] = [];
  listaItensCompraCopy: ItemListaCompraRequest[] = [];
  listaCategoriasFiltro: CategoriaModel[] = []
  listaCategoriasExibicao: CategoriaModel[] = [];
  listaFiltroCompra: string[] = ['Todos os itens', 'Apenas necessário'];

  exibirFiltroCategoria: boolean = false;
  exibirFiltroListaCompras: boolean = false;
  estoqueModificado: boolean = false;
  exibirTodosItens: boolean = true;
  itensNaLista: boolean = false;

  emojiListaVazia: string = '✅';
  tituloListaVazia: string = 'Tudo em dia!';
  descricaoListaVazia: string = 'Nenhum item precisa ser comprado no momento.';
  termoBuscado: string = '';


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
    private categoriaService: CategoriaService,
    private listaService: ListaService) { }

  ngOnInit(): void {
    this.iniciarVariaveis();
    this.carregarItens();
  }

  iniciarVariaveis() {

    this.listaCategoriasFiltro = [this.categoriaFiltro];
    this.listaCategoriasExibicao = [];

    this.termoBuscado = '';
    this.categoriaSelecionada = this.categoriaFiltro;

  }

  carregarItens() {
    this.capturarItensCompra();
    this.capturarCategorias()
  }



  capturarItensCompra() {

    this.listaService.getLista().subscribe({
      next: (data: ItemListaCompraRequest[]) => {
        this.listaItensCompra = data.map(c => ({
          ...c,
          novoValor: c.quantidadeSugerida
        }));


        this.listaItensCompra.forEach(item => {
          const categoriaItem = item.categoria

          if (this.listaCategoriasExibicao.filter(c => c.id === categoriaItem.id).length === 0) {
            this.listaCategoriasExibicao.push(categoriaItem);
          }
        })

        this.listaItensCompraCopy = this.listaItensCompra;

        this.estoqueModificado = this.isAlteracaoDetectada();
        this.itensNaLista = this.isItensNaLista();

      },
      error: (err) => {

        console.error(err.error);

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

  capturarPDF() {
    const listaFinal: ItemListaCompraResponse[] = [];

    this.listaItensCompra.forEach(item => {
      const itemResponse: ItemListaCompraResponse = {
        ideItem: item.ideItem,
        nome: item.nomeItem,
        categoria: item.categoria,
        unidadeMedida: item.unidadeMedida,
        quantidadeCompra: item.novoValor
      }

      listaFinal.push(itemResponse);
    });

    this.listaService.postPDF(listaFinal).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = `lista_de_compras_${new Date().toISOString().split('T')[0]}.pdf`;
        a.click();

        window.URL.revokeObjectURL(url);


      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  onBusca(termo?: string) {

    let listaFinal: ItemListaCompraRequest[] = this.listaItensCompra;

    if (termo !== undefined) {
      this.termoBuscado = termo;
    }

    if (!this.exibirTodosItens) {
      listaFinal = listaFinal.filter(item => item.novoValor > 0)

    }

    if (this.categoriaSelecionada.id !== 0) {

      listaFinal = listaFinal.filter(item => item.categoria.id === this.categoriaSelecionada.id)

    }

    if (this.termoBuscado !== '') {

      listaFinal = listaFinal.filter(item => item.nomeItem.toLowerCase().includes(this.termoBuscado.toLowerCase()))

    }

    this.listaItensCompraCopy = listaFinal;

    this.listaCategoriasExibicao = [];
    this.listaItensCompraCopy.forEach(item => {
      const categoriaItem = item.categoria

      if (this.listaCategoriasExibicao.filter(c => c.id === categoriaItem.id).length === 0) {
        this.listaCategoriasExibicao.push(categoriaItem);
      }
    })


  }

  toggleSelecionarCategoriaFiltro(categoria: CategoriaModel) {

    this.categoriaSelecionada = categoria;
    this.exibirFiltroCategoria = false;

    this.onBusca();

  }

  toggleSelecionarOpcaoFiltroCompra(opcao: boolean) {

    this.exibirTodosItens = opcao;
    this.exibirFiltroListaCompras = false;

    this.onBusca();

  }

  isAlteracaoDetectada() {
    return this.listaItensCompraCopy.filter(i => i.quantidadeSugerida !== i.novoValor).length > 0;
  }

  isItensNaLista() {
    return this.listaItensCompraCopy.filter(i => i.novoValor > 0).length > 0;
  }

  aumentarValor(item: ItemListaCompraRequest) {
    item.novoValor = Number(item.novoValor) + 1;
    const itemListaPrincipal = this.listaItensCompra.filter(i => i.ideItem === item.ideItem)[0];
    itemListaPrincipal.novoValor = item.novoValor;

    this.estoqueModificado = this.isAlteracaoDetectada();
    this.itensNaLista = this.isItensNaLista();
  }

  diminuirValor(item: ItemListaCompraRequest) {
    if (item.novoValor > 0) {
      item.novoValor = Number(item.novoValor) - 1;
      const itemListaPrincipal = this.listaItensCompra.filter(i => i.ideItem === item.ideItem)[0];
      itemListaPrincipal.novoValor = item.novoValor;

      this.estoqueModificado = this.isAlteracaoDetectada();
      this.itensNaLista = this.isItensNaLista();
    }
  }

  resetarValor(item: ItemListaCompraRequest) {
    item.novoValor = Number(item.quantidadeSugerida);
    const itemListaPrincipal = this.listaItensCompra.filter(i => i.ideItem === item.ideItem)[0];
    itemListaPrincipal.novoValor = item.novoValor;

    this.estoqueModificado = this.isAlteracaoDetectada();
    this.itensNaLista = this.isItensNaLista();

  }

  resetarTudo() {
    this.listaItensCompra = this.listaItensCompra.map(c => ({
      ...c,
      novoValor: c.quantidadeSugerida
    }));

    this.listaItensCompraCopy = this.listaItensCompra;

    this.estoqueModificado = this.isAlteracaoDetectada();
    this.itensNaLista = this.isItensNaLista();


  }


}


