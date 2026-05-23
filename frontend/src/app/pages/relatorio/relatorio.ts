import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RelatorioService } from '../../services/relatorio';
import { ItemService } from '../../services/item';
import { PageTitleComponent } from '../../components/page-title/page-title';
import { IntervaloAnosModel } from '../../models/intervalo-anos.model';
import { ToastMessageErrorComponent } from '../../components/toast-message-error/toast-message-error';
import { ItemModel } from '../../models/item.model';
import { RelatorioMensalModel } from '../../models/relatorio-mensal.model';
import * as XLSX from 'xlsx';
import { RelatorioGastoModel } from '../../models/relatorio-gasto.model';

@Component({
  selector: 'app-relatorio',
  imports: [CommonModule, FormsModule, PageTitleComponent, ToastMessageErrorComponent],
  templateUrl: './relatorio.html',
  styleUrl: './relatorio.css',
})
export class Relatorio implements OnInit {

  listaMeses: { numero: number; nome: string }[] = [
    { numero: 1, nome: "Janeiro" },
    { numero: 2, nome: "Fevereiro" },
    { numero: 3, nome: "Março" },
    { numero: 4, nome: "Abril" },
    { numero: 5, nome: "Maio" },
    { numero: 6, nome: "Junho" },
    { numero: 7, nome: "Julho" },
    { numero: 8, nome: "Agosto" },
    { numero: 9, nome: "Setembro" },
    { numero: 10, nome: "Outubro" },
    { numero: 11, nome: "Novembro" },
    { numero: 12, nome: "Dezembro" },
  ];
  listaAnos: number[] = [];
  listaItens: ItemModel[] = [];

  itemSelecionado: ItemModel = {} as ItemModel;

  exibirListaMeses: boolean = false;
  exibirListaAnos: boolean = false;
  exibirListaItem: boolean = false;
  relatorioComprasAtivo: boolean = true;
  relatorioGastosAtivo: boolean = false;
  exibirToastErro: boolean = false;

  tituloRelatorio: string = '';
  tituloRelatorioCompra: string = 'Itens comprados no mês';
  tituloRelatorioGasto: string = 'Gastos por item';
  mesSelecionado: { numero: number; nome: string } = this.listaMeses[new Date().getMonth()];
  dataInicial: string = '';
  dataFinal: string = '';
  mensagemToast: string = '';

  anoSelecionado: number = new Date().getFullYear();





  constructor(
    private relatorioService: RelatorioService,
    private itemService: ItemService
  ) { }
  ngOnInit(): void {

    this.capturarIntervaloDeAnosCompra();
    this.capturarItens();
    this.tituloRelatorio = this.tituloRelatorioCompra;

  }

  capturarIntervaloDeAnosCompra() {
    this.relatorioService.getIntervaloAnosCompra().subscribe({
      next: (data: IntervaloAnosModel) => {
        if (data.anoInicio === 0 || data.anoFim === 0) {
          this.listaAnos.push(new Date().getFullYear());
        } else {
          for (let i = data.anoInicio; i <= data.anoFim; i++) {
            this.listaAnos.push(i);
          }
        }

        this.anoSelecionado = this.listaAnos[0];

      },
      error: (err) => {
        console.error(err);
      }
    })
  }

  capturarItens() {

    this.itemService.getItens().subscribe({
      next: (data: ItemModel[]) => {
        this.listaItens = data;
        this.itemSelecionado = this.listaItens[0];

      },
      error: (err) => {
        console.error(err);

      }
    })

  }

  gerarRelatorioCompra() {

    this.relatorioService.getRelatorioMensal(this.anoSelecionado, this.mesSelecionado.numero).subscribe({
      next: (data: RelatorioMensalModel[]) => {
        if (data.length === 0) {
          this.mensagemToast = 'Não foram encontrados registros para os parâmetros selecionados.';
          this.exibirToastErro = true;
          setTimeout(() => {
            this.exibirToastErro = false;
          }, 5000);

        } else {
          this.exportarPlanilhaCompra(data);

        }
      },
      error: (err) => {
        this.mensagemToast = err.error;
        this.exibirToastErro = true;
        setTimeout(() => {
          this.exibirToastErro = false;
        }, 5000);

      }
    })

  }

  gerarRelatorioGasto() {

    this.relatorioService.getRelatorioGasto(this.itemSelecionado.id, this.dataInicial, this.dataFinal).subscribe({
      next: (data: RelatorioGastoModel) => {

        if (data.gastoTotal === 0) {
          this.mensagemToast = 'Não foram encontrados registros para os parâmetros selecionados.';
          this.exibirToastErro = true;
          setTimeout(() => {
            this.exibirToastErro = false;
          }, 5000);

        } else {
          this.exportarPlanilhaGasto(data);

        }

      },
      error: (err) => {
        this.mensagemToast = err.error;
        this.exibirToastErro = true;
        setTimeout(() => {
          this.exibirToastErro = false;
        }, 5000);

      }
    })

  }

  exportarPlanilhaCompra(items: RelatorioMensalModel[]) {
    const rows = items.map(i => ({
      Nome: i.nomeItem,
      Marca: i.marca,
      Preço: i.preco,
      Quantidade: i.quantidade,
      Total: i.preco * i.quantidade,
    }));

    const ws = XLSX.utils.json_to_sheet(rows);

    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Lista');

    XLSX.writeFile(wb, `compras-${this.mesSelecionado.numero}-${this.anoSelecionado}_${new Date().toISOString().slice(0, 10)}.xlsx`);
  }

  exportarPlanilhaGasto(item: RelatorioGastoModel) {

    const dados = [
      ['Relatório de Gastos'],
      [],
      ['Data Inicial', this.formatarDataBR(this.dataInicial)],
      ['Data Final', this.formatarDataBR(this.dataFinal)],
      ['Gasto Total', item.gastoTotal],
      [],
      ['Data Compra', 'Marca', 'Valor Pago']
    ];


    item.historico.forEach((item: any) => {
      dados.push([
        this.formatarDataBR(item.dataCompra),
        item.marca,
        item.valorTotalPago
      ]);
    });

    const ws = XLSX.utils.aoa_to_sheet(dados);

    const wb = XLSX.utils.book_new();

    XLSX.utils.book_append_sheet(
      wb,
      ws,
      'Relatório'
    );

    XLSX.writeFile(
      wb,
      `gastos-${this.itemSelecionado.nome}_${new Date().toISOString().slice(0, 10)
      }.xlsx`
    );
  }

  toggleSelecionarMes(mes: { numero: number; nome: string }) {
    this.mesSelecionado = mes;
    this.exibirListaMeses = false;
  }

  toggleSelecionarAno(ano: number) {
    this.anoSelecionado = ano;
    this.exibirListaAnos = false;
  }

  toggleSelecionarItem(item: ItemModel) {
    this.itemSelecionado = item;
    this.exibirListaItem = false;

  }

  togglePerspectiva() {
    this.relatorioComprasAtivo = !this.relatorioComprasAtivo;
    this.relatorioGastosAtivo = !this.relatorioGastosAtivo;

    this.tituloRelatorio = this.relatorioComprasAtivo ? this.tituloRelatorioCompra : this.tituloRelatorioGasto;
  }

  formatarDataBR(data: string): string {
    const [ano, mes, dia] = data.split('-');

    return `${dia}/${mes}/${ano}`;
  }


}
