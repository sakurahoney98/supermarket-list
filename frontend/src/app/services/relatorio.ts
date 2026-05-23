import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { IntervaloAnosModel } from '../models/intervalo-anos.model';
import { RelatorioMensalModel } from '../models/relatorio-mensal.model';
import { RelatorioGastoModel } from '../models/relatorio-gasto.model';

@Injectable({
  providedIn: 'root',
})
export class RelatorioService {
  private apiRelatorio = 'http://localhost:8080/relatorio';

  constructor(private http: HttpClient) { }

  getIntervaloAnosCompra(){
    return this.http.get<IntervaloAnosModel>(`${this.apiRelatorio}/anos`)
  }

  getRelatorioMensal(ano: number, mes: number) {
    const params = new HttpParams().set('ano', ano).set('mes', mes);

    return this.http.get<RelatorioMensalModel[]>(`${this.apiRelatorio}/mensal`, { params });

  }

  getRelatorioGasto(ideItem: number, dataInicio: string, dataFim: string) {
    const params = new HttpParams()
      .set('ideItem', ideItem)
      .set('inicio', dataInicio)
      .set('fim', dataFim);

    return this.http.get<RelatorioGastoModel>(`${this.apiRelatorio}/gasto`, { params });
  }
}
