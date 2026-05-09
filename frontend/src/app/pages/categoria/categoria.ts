import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CategoriaService } from '../../services/categoria';
import { SugestaoService } from '../../services/sugestao';
import { CategoriaModel } from '../../models/categoria.model';
import { SugestaoModel } from '../../models/sugestao.model';
import { CategoriaRequestModel } from '../../models/categoria-request.model';
import { PageTitleComponent } from '../../components/page-title/page-title';
import { ModalComponent } from '../../components/modal/modal';
import { ListActionComponent } from '../../components/list-action/list-action';
import { ToastMessageSucessComponent } from '../../components/toast-message-sucess/toast-message-sucess';
import { ToastMessageErrorComponent } from '../../components/toast-message-error/toast-message-error';
import { EmptyListComponent } from '../../components/empty-list/empty-list';
import { SearchBarComponent } from '../../components/search-bar/search-bar';




@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [PageTitleComponent, ListActionComponent, CommonModule, ModalComponent, FormsModule, ToastMessageSucessComponent, ToastMessageErrorComponent, EmptyListComponent, SearchBarComponent],
  templateUrl: './categoria.html',
  styleUrl: './categoria.css',
})
export class Categoria implements OnInit {

  listaCategoria: CategoriaModel[] = [];
  listaSugestoes: SugestaoModel[] = [];

  todosItensSelecionados: boolean = false;
  exibirModal: boolean = false;
  exibirModalSugestoes: boolean = false;
  exibirToastSucesso: boolean = false;
  exibirToastErro: boolean = false;
  nomePreenchido: boolean = true;
  corLetraPreenchido: boolean = true;
  corFundoPreenchido: boolean = true;

  quantidadeSelecionada: number = 0;

  corLetra: string = '#000000';
  corFundo: string = '#FFFFFF';
  exemploTextoPreview: string = 'Categoria';
  mensagemToast: string = '';
  emojiListaVazia: string = '🏷️';
  tituloListaVazia: string = 'Nenhuma categoria encontrada';
  descricaoListaVazia: string = 'Crie sua primeira categoria para organizar seus produtos.';




  constructor(
    private categoriaService: CategoriaService,
    private sugestaoService: SugestaoService
  ) { }



  ngOnInit() {

    this.capturarCategorias();
    this.capturarSugestoes();

  }

  capturarCategorias() {
    this.categoriaService.getCategorias().subscribe({
      next: (data: CategoriaModel[]) => {
        this.listaCategoria = data;
        this.listaCategoria = data.map(c => ({
          ...c,
          selecionado: false
        }));


      },
      error: (err) => {
        console.log(err)
      }

    });


  }

  capturarSugestoes() {
    this.sugestaoService.getSugestoes().subscribe({
      next: (data: SugestaoModel[]) => {
        this.listaSugestoes = data;

      },

      error: (err) => {
        console.log(err);
      }
    })
  }

  cadastrarCategoria() {

    this.nomePreenchido = true;
    this.corLetraPreenchido = true;
    this.corFundoPreenchido = true;

    if (this.exemploTextoPreview === '') {
      this.nomePreenchido = false;

    }
    if (this.corLetra === '') {
      this.corLetraPreenchido = false;

    }
    if (this.corFundo === '') {
      this.corFundoPreenchido = false;

    }

    if (this.nomePreenchido && this.corLetraPreenchido && this.corFundoPreenchido) {
      const categoria: CategoriaRequestModel = {
        nome: this.exemploTextoPreview,
        corLetra: this.corLetra,
        corFundo: this.corFundo,
      } as CategoriaRequestModel;

      this.categoriaService.inserirCategoria(categoria).subscribe({
        next: () => {
          this.mensagemToast = "Categoria cadastrada com sucesso!";

          this.exibirToastSucesso = true;
          setTimeout(() => {
            this.exibirToastSucesso = false;
          }, 5000);

          this.exemploTextoPreview = '';
          this.corLetra = '#000000';
          this.corFundo = '#FFFFFF';
          this.onFecharModal();
          this.capturarCategorias();



        },
        error: (err) => {
          this.mensagemToast = err.error;
          this.exibirToastErro = true;
          setTimeout(() => {
            this.exibirToastErro = false;
          }, 5000);
        }
      });
    } else {
      this.mensagemToast = "Preencha os campos obrigatórios";
      this.exibirToastErro = true;
      setTimeout(() => {
        this.exibirToastErro = false;
      }, 5000);
    }

  }

  deletarCategoria() {
    const listaSelecionados = this.listaCategoria.filter(c => c.selecionado);

    if (listaSelecionados.length === 0) return;

    if (listaSelecionados.length < 2) {
      const categoria = listaSelecionados[0];
      const ideCategoria = categoria.id;

      this.categoriaService.deleteCategoria(ideCategoria).subscribe({
        next: () => {
          this.listaCategoria = this.listaCategoria.filter(c => c.id !== ideCategoria);

          this.atualizarQuantidadeSelecionada();

          this.mensagemToast = "Categoria deletada com sucesso!";

          this.exibirToastSucesso = true;

          setTimeout(() => {
            this.exibirToastSucesso = false;
          }, 5000);


        },
        error: (err) => {
          this.mensagemToast = err.error;
          this.exibirToastErro = true;
          setTimeout(() => {
            this.exibirToastErro = false;
          }, 5000);
        }
      })

    } else {
      const ids = listaSelecionados.map(c => c.id);

      this.categoriaService.deleteCategoriaEmMassa(ids).subscribe({
        next: () => {
          this.listaCategoria = this.listaCategoria.filter(c => !ids.includes(c.id));

          this.atualizarQuantidadeSelecionada();

          this.mensagemToast = "Categorias deletadas com sucesso!";

          this.exibirToastSucesso = true;

          setTimeout(() => {
            this.exibirToastSucesso = false;
          }, 5000);



        },
        error: (err) => {
          this.mensagemToast = this.formatarMensagem(err.error);
          this.exibirToastErro = true;
          setTimeout(() => {
            this.exibirToastErro = false;
          }, 5000);
        }
      })

    }

  }

  onBusca(termo: string) {
    this.categoriaService.buscarTermo(termo).subscribe({
      next: (data: CategoriaModel[]) => {
        this.listaCategoria = data.map(c => ({
          ...c,
          selecionado: false
        }));

        this.atualizarQuantidadeSelecionada();

      },
      error: (err) => {
        this.mensagemToast = err.error;
        this.exibirToastErro = true;
        setTimeout(() => {
          this.exibirToastErro = false;
        }, 5000);
      }
    });
  }

  onSelecionarTudo(selecionar: boolean) {
    this.listaCategoria.forEach(categoria => {
      categoria.selecionado = selecionar;
    });

    this.atualizarQuantidadeSelecionada();
  }

  onAbrirModal() {
    this.exibirModal = true;
  }
  onFecharModal() {
    this.exibirModal = false;
  }


  toggleModalSugestoes() {
    this.exibirModalSugestoes = !this.exibirModalSugestoes;
  }

  atualizarQuantidadeSelecionada() {
    this.quantidadeSelecionada = this.listaCategoria.filter(c => c.selecionado).length;

    this.isTodosItensSelecionados();
  }

  isTodosItensSelecionados() {
    this.todosItensSelecionados = this.listaCategoria.length == this.quantidadeSelecionada;
  }

  selecionarSugestao(s: SugestaoModel) {
    this.corFundo = s.corFundo;
    this.corLetra = s.corLetra;

    this.exibirModalSugestoes = false;

  }


  formatarMensagem(mensagem: string): string {

    const inicio = mensagem.indexOf("[");
    const prefixoMensagem = mensagem.substring(0, inicio);


    let mensagemFormatada = prefixoMensagem + mensagem.substring(inicio + 1, mensagem.lastIndexOf("]"));

    mensagemFormatada = mensagemFormatada.replaceAll("],", "]\n");

    return mensagemFormatada;


  }



}
