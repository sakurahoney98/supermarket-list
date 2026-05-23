import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PageTitleComponent } from '../../components/page-title/page-title';
import { ToastMessageSucessComponent } from '../../components/toast-message-sucess/toast-message-sucess';
import { ModalComponent } from '../../components/modal/modal';
import { ItemService } from '../../services/item';
import { CompraService } from '../../services/compra';
import { ItemCompraModel } from '../../models/item-compra.model';
import { ItemModel } from '../../models/item.model';
import { ItemCompraResponseModel } from '../../models/item-compra-response.model';
import { CompraRequestModel } from '../../models/compra-request.model';
import { CompraResponseModel } from '../../models/compra-response.model';

@Component({
  selector: 'app-compra',
  imports: [CommonModule, FormsModule, PageTitleComponent, ToastMessageSucessComponent, ModalComponent],
  templateUrl: './compra.html',
  styleUrl: './compra.css',
})
export class Compra implements OnInit {

  itensNaCompra: ItemCompraModel[] = [];
  listaItens: ItemModel[] = [];
  listaOpcoesConflito: CompraRequestModel[] = [];
  opcaoConflito: CompraRequestModel = {
    ideCompra: 0,
    quantidadeItens: 0
  };
  opcaoConflitoSelecionada: CompraRequestModel = this.opcaoConflito;


  exibirFiltro: boolean = false;
  exibirToastSucesso: boolean = false;
  exibirModal: boolean = false;
  exibirListaResolverConflito: boolean = false;

  dataCompra: string = this.converterDataParaString(new Date());
  termoBuscado: string = ''
  mensagemToast: string = '';

  totalCompra: number = 0;

  constructor(private itemService: ItemService,
    private compraService: CompraService
  ) { }

  ngOnInit(): void {
    this.capturarItens();

  }

  capturarItens() {

    this.itemService.getItens().subscribe({
      next: (data: ItemModel[]) => {
        this.listaItens = data.map(c => ({
          ...c,
          selecionado: false
        }));

        this.montarListaItensComprados();



      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  montarListaItensComprados() {
  
    let rascunho: ItemCompraModel[] = JSON.parse(localStorage.getItem("listaInserirCompra") || '[]');

    if (rascunho.length > 0) {
      const idsItens = new Set(this.listaItens.map(item => item.id));
      rascunho = rascunho.filter(item => idsItens.has(item.ideItem));


    }

    this.itensNaCompra = rascunho;
    this.calcularTotal();



  }

  inserirItemNaLista(item: ItemModel) {
    const itemCompra = {
      id: this.itensNaCompra.length === 0 ? 1 : this.itensNaCompra[this.itensNaCompra.length - 1].id + 1,
      ideItem: item.id,
      nomeItem: item.nome,
      quantidadeComprada: 1,
      valor: 1,
      marca: '',
      subtotal: 1
    };

    this.itensNaCompra.push(itemCompra);
    this.termoBuscado = '';
    this.exibirFiltro = false;

    this.calcularTotal();
  }

  removerDaLista(item: ItemCompraModel) {
    this.itensNaCompra = this.itensNaCompra.filter(i => i !== item);

    this.calcularTotal();

  }

  finalizar() {


    this.compraService.getCapturarComprasNaData(this.dataCompra).subscribe({
      next: (data: CompraRequestModel[]) => {

        if (data.length > 0) {
          this.exibirModal = true;

          this.listaOpcoesConflito = [this.opcaoConflito, ...data];


        } else {
          this.inserirCompra();

        }

      },
      error: (err) => {

        console.error(err);
      }
    });



  }

  resolverConflito() {
    if (this.opcaoConflitoSelecionada.ideCompra === 0) {
      this.inserirCompra();
    } else {
      this.unirCompra();
    }
  }

  inserirCompra() {
    const compra = this.montarCompra();
    this.compraService.postInserirCompra(compra).subscribe({
      next: () => {
        this.exibirModal = false;
        this.exibirListaResolverConflito = false;
        this.resetarTudo();

        this.mensagemToast = "Compra inserida com sucesso!";

        this.exibirToastSucesso = true;
        setTimeout(() => {
          this.exibirToastSucesso = false;
        }, 5000);



      }
    })
  }

  unirCompra() {
    const compra = this.montarCompra();

    this.compraService.postUnirCompra(compra, this.opcaoConflitoSelecionada.ideCompra).subscribe({
      next: () => {

        this.exibirModal = false;
        this.exibirListaResolverConflito = false;
        this.resetarTudo();

        this.mensagemToast = "Compra inserida com sucesso!";

        this.exibirToastSucesso = true;
        setTimeout(() => {
          this.exibirToastSucesso = false;
        }, 5000);

      },
      error: (err) => {
        console.error(err);

      },
    })

  }

  salvarRascunho() {
    localStorage.setItem('listaInserirCompra', JSON.stringify(this.itensNaCompra));

    this.mensagemToast = "Rascunho salvo com sucesso!";

    this.exibirToastSucesso = true;
    setTimeout(() => {
      this.exibirToastSucesso = false;
    }, 5000);


  }

  resetarTudo() {
    this.itensNaCompra = [];
    this.totalCompra = 0;
    this.listaOpcoesConflito = [];
    this.opcaoConflitoSelecionada = this.opcaoConflito;
    localStorage.removeItem("listaInserirCompra");
  }

  onBusca() {

    if (this.termoBuscado !== '') {
      this.itemService.buscarItensPorNome(this.termoBuscado).subscribe({
        next: (data: ItemModel[]) => {
          this.listaItens = data.map(c => ({
            ...c,
            selecionado: false
          }));

          this.exibirFiltro = true;
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else {
      this.capturarItens();
      this.exibirFiltro = false;
    }





  }
  onAbrirModal() {
    this.exibirModal = true;
  }
  onFecharModal() {
    this.exibirModal = false;
    this.exibirListaResolverConflito = false
  }

  toggleAlterarOpcaoConflito(opcao: CompraRequestModel) {
    this.opcaoConflitoSelecionada = opcao;
    this.exibirListaResolverConflito = false;
  }

  private montarCompra() {
    let listaItensDaCompra = [] as ItemCompraResponseModel[];

    this.itensNaCompra.forEach(item => {
      const itemCompraResponse = {
        ideItem: item.ideItem,
        quantidadeComprada: item.quantidadeComprada,
        valor: item.valor,
        marca: item.marca
      }

      listaItensDaCompra.push(itemCompraResponse);
    })
    const compra = {
      dataCompra: this.converterStringParaData(this.dataCompra),
      listaItens: listaItensDaCompra

    } as CompraResponseModel;

    return compra;
  }

  private converterDataParaString(data: Date): string {
    const yyyy = data.getFullYear();
    const mm = String(data.getMonth() + 1).padStart(2, '0');
    const dd = String(data.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;

  }

  private converterStringParaData(dataStr: string): Date {
    const [yyyy, mm, dd] = dataStr.split('-').map(Number);
    return new Date(yyyy, mm - 1, dd);
  }

  formatarDecimalParaReal(valorRecebido: number): string {
    return valorRecebido
      ? new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(Number(valorRecebido))
      : 'R$ 0,00'
  }

  calcularTotal() {
    this.totalCompra = this.itensNaCompra.reduce((acc, item) => acc + item.subtotal, 0);

  }

  calcularValorTotalItem(item: ItemCompraModel) {
    item.subtotal = parseFloat((item.quantidadeComprada * item.valor).toFixed(2));
    this.calcularTotal();
  }




}
