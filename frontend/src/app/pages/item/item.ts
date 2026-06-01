import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ItemService } from '../../services/item';
import { CategoriaService } from '../../services/categoria';

import { PageTitleComponent } from '../../components/page-title/page-title';
import { ModalComponent } from '../../components/modal/modal';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { EmptyListComponent } from '../../components/empty-list/empty-list';
import { ListActionComponent } from '../../components/list-action/list-action';
import { ToastMessageErrorComponent } from '../../components/toast-message-error/toast-message-error';
import { ToastMessageSucessComponent } from '../../components/toast-message-sucess/toast-message-sucess';

import { ItemModel } from '../../models/item.model';
import { CategoriaModel } from '../../models/categoria.model';
import { ItemRequestModel } from '../../models/item-request.model';


@Component({
  selector: 'app-item',
  standalone: true,
  imports: [PageTitleComponent, ModalComponent, SearchBarComponent, EmptyListComponent, ListActionComponent, ToastMessageErrorComponent, ToastMessageSucessComponent, CommonModule, FormsModule],
  templateUrl: './item.html',
  styleUrl: './item.css',
})
export class Item implements OnInit {

  listaItens: ItemModel[] = [];
  listaCategorias: CategoriaModel[] = []
  listaCategoriasFiltro: CategoriaModel[] = []

  categoriaFiltro: CategoriaModel = {
    id: 0,
    nome: 'Todas categorias',
    ativo: true,
    corLetra: '',
    corFundo: '',
    selecionado: false
  };
  categoriaSelecionada: CategoriaModel = this.categoriaFiltro;
  categoriaCadastroSelecionada: CategoriaModel = {} as CategoriaModel;
  categoriaEdicaoSelecionada: CategoriaModel = {} as CategoriaModel;
  itemEdicao: ItemModel = {} as ItemModel;
  itemCadastro: ItemRequestModel = {} as ItemRequestModel;

  todosItensSelecionados: boolean = false;
  exibirModalCadastro: boolean = false;
  exibirModalEdicao: boolean = false;
  exibirFiltro: boolean = false;
  exibirListaCategoria: boolean = false;
  exibirToastSucesso: boolean = false;
  exibirToastErro: boolean = false;
  nomePreenchido: boolean = true;
  estoquePreenchido: boolean = true;
  maximoCompraPreenchido: boolean = true;
  duracaoPreenchido: boolean = true;
  ultimaCompraPreenchido: boolean = true;


  quantidadeSelecionada: number = 0;
  estoque: number | null = null;
  maximoCompra: number | null = null;
  duracao: number | null = null;


  unidadePeso: string = '';
  mensagemToast: string = '';
  emojiListaVazia: string = '📦';
  tituloListaVazia: string = 'Nenhum item encontrado';
  descricaoListaVazia: string = 'Crie seu primeiro item para começar.';
  termoBuscado: string = '';

  ultimaCompra: Date | null = null;


  constructor(
    private itemService: ItemService,
    private categoriaService: CategoriaService) { }

  ngOnInit() {
    this.capturarItens();
    this.capturarCategorias();
    this.listaCategoriasFiltro.push(this.categoriaFiltro);


  }


  capturarItens() {

    this.itemService.getItens().subscribe({
      next: (data: ItemModel[]) => {
        this.listaItens = data.map(c => ({
          ...c,
          selecionado: false
        }));


      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  capturarCategorias() {

    this.categoriaService.getCategorias().subscribe({
      next: (data: CategoriaModel[]) => {
        this.listaCategorias = data;
        this.listaCategoriasFiltro.push(...data);
        this.categoriaCadastroSelecionada = this.listaCategorias[0];
        this.itemCadastro.categoria = this.listaCategorias[0].id;


      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  cadastrarItem() {

    this.resetarEstadoInput();

    if (this.isCamposObrigatoriosPreenchidos(this.itemCadastro)) {

      this.itemService.inserirItem(this.itemCadastro).subscribe({
        next: () => {

          this.exibirMensagemDeSucesso("Item cadastrado com sucesso");

          this.onFecharModal();
          this.capturarItens();
          this.itemCadastro = {} as ItemRequestModel;
          this.itemCadastro.categoria = this.listaCategorias[0].id ?? this.categoriaFiltro.id;


        },
        error: (err) => {
          this.exibirMensagemDeErro(err.error);

        }

      })
    } else {

      this.exibirMensagemDeErro("Preencha todos os campos obrgatórios.");
    }

  }

  deletarItens() {
    const listaSelecionados = this.listaItens.filter(i => i.selecionado);

    if (listaSelecionados.length === 0) return;

    if (listaSelecionados.length < 2) {
      const item = listaSelecionados[0];
      const ideItem = item.id;

      this.itemService.deleteItem(ideItem).subscribe({
        next: () => {
          this.listaItens = this.listaItens.filter(i => i.id !== ideItem);

          this.atualizarQuantidadeSelecionada();

          this.exibirMensagemDeSucesso("Item deletado com sucesso!");


        },
        error: (err) => {
          this.exibirMensagemDeErro(err.error);
        }
      })

    } else {
      const ids = listaSelecionados.map(i => i.id);

      this.itemService.deleteItemEmMassa(ids).subscribe({
        next: () => {
          this.listaItens = this.listaItens.filter(i => !ids.includes(i.id));

          this.atualizarQuantidadeSelecionada();

          this.exibirMensagemDeSucesso("Itens deletados com sucesso!");




        },
        error: (err) => {
          this.exibirMensagemDeErro(err.error);
        }
      })

    }

  }

  editarItem() {
    this.resetarEstadoInput();

    if (this.isCamposObrigatoriosPreenchidos(this.itemEdicao)) {

      const itemEditado: ItemRequestModel = {
        nome: this.itemEdicao.nome,
        unidadeMedida: this.itemEdicao.unidadeMedida,
        quantidadeEstoque: this.itemEdicao.quantidadeEstoque,
        limiteCompra: this.itemEdicao.limiteCompra,
        dataUltimaCompra: this.itemEdicao.dataUltimaCompra,
        categoria: this.categoriaEdicaoSelecionada.id,
        duracaoDias: this.itemEdicao.duracaoDias,
      };

      this.itemService.editarItem(itemEditado, this.itemEdicao.id).subscribe({
        next: () => {
          this.capturarItens();

          this.exibirMensagemDeSucesso(`Item ${itemEditado.nome} salvo com sucesso.`);

        },
        error: (err) => {
          this.exibirMensagemDeErro(err.error);
        }
      })

    }

  }

  onBusca(termo?: string) {

    if (termo !== undefined) {
      this.termoBuscado = termo;
    }

    if (this.categoriaSelecionada.id !== 0 && this.termoBuscado !== '') {

      this.itemService.buscarItensPorNomeNaCategoria(this.termoBuscado, this.categoriaSelecionada.id).subscribe({
        next: (data: ItemModel[]) => {
          this.listaItens = data.map(c => ({
            ...c,
            selecionado: false
          }));
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else if (this.categoriaSelecionada.id !== 0) {

      this.itemService.buscarItensPorCategoria(this.categoriaSelecionada.id).subscribe({
        next: (data: ItemModel[]) => {

          this.listaItens = data.map(c => ({
            ...c,
            selecionado: false
          }));
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else if (this.termoBuscado !== '') {
      this.itemService.buscarItensPorNome(this.termoBuscado).subscribe({
        next: (data: ItemModel[]) => {
          this.listaItens = data.map(c => ({
            ...c,
            selecionado: false
          }));
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else {
      this.capturarItens();
    }

  }

  onSelecionarTudo(selecionar: boolean) {
    this.listaItens.forEach(item => {
      item.selecionado = selecionar;
    });

    this.atualizarQuantidadeSelecionada();

  }

  onAbrirModal() {
    if (this.listaCategorias.length === 0) {

      this.exibirMensagemDeErro("Necessário cadastrar uma categoria antes de cadastrar item.");
    } else {
      this.exibirModalCadastro = true;
      this.exibirFiltro = false;
    }

  }
  onFecharModal() {

    this.exibirModalCadastro = false;
    this.exibirListaCategoria = false;

    this.resetarEstadoInput();

  }

  onAbrirModalEdicao(item: ItemModel) {

    this.itemEdicao = item;
    this.categoriaEdicaoSelecionada = item.categoria;

    this.exibirModalEdicao = true;
    this.exibirFiltro = false;
    this.resetarEstadoInput();


  }

  onFecharModalEdicao() {
    this.exibirModalEdicao = false;
    this.exibirListaCategoria = false;

  }

  toggleSelecionarCategoriaFiltro(categoria: CategoriaModel) {
    this.categoriaSelecionada = categoria;
    this.exibirFiltro = false;

    this.onBusca();

  }

  toggleSelecionarCategoriaCadastro(categoria: CategoriaModel) {

    this.categoriaCadastroSelecionada = categoria;
    this.exibirListaCategoria = false;

  }

  toggleSelecionarCategoriaEdicao(categoria: CategoriaModel) {

    this.categoriaEdicaoSelecionada = categoria;
    this.exibirListaCategoria = false;

  }


  atualizarQuantidadeSelecionada() {

    this.quantidadeSelecionada = this.listaItens.filter(i => i.selecionado).length;

    this.isTodosItensSelecionados();

  }

  resetarEstadoInput() {

    this.nomePreenchido = true;
    this.estoquePreenchido = true;
    this.maximoCompraPreenchido = true;
    this.duracaoPreenchido = true;
    this.ultimaCompraPreenchido = true;

  }

  isTodosItensSelecionados() {

    this.todosItensSelecionados = this.listaItens.length == this.quantidadeSelecionada;

  }

  isCamposObrigatoriosPreenchidos(item: ItemRequestModel | ItemModel) {
    let validado = true;
    if (item.nome === undefined || item.nome.trim() === '') {
      this.nomePreenchido = false;
      validado = false;
    }
    if (item.quantidadeEstoque === undefined || item.quantidadeEstoque < 0) {
      this.estoquePreenchido = false;
      validado = false;

    }
    if (item.limiteCompra === undefined || item.limiteCompra < 1) {
      this.maximoCompraPreenchido = false;
      validado = false;
    }
    if (item.duracaoDias === undefined || item.duracaoDias < 1) {
      this.duracaoPreenchido = false;
      validado = false;
    }
    if (item.dataUltimaCompra === undefined) {
      this.ultimaCompraPreenchido = false;
      validado = false;
    }



    return validado;

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
