import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { CompraRequestModel } from '../models/compra-request.model';
import { CompraResponseModel } from '../models/compra-response.model';

import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class CompraService {

  private apiCompra = `${environment.apiUrl}/compra`

  constructor( private http: HttpClient) {}

  getCapturarComprasNaData(data: string) {
    return this.http.get<CompraRequestModel[]>(`${this.apiCompra}/${data}`);
  }

  postInserirCompra(compra: CompraResponseModel) {
    return this.http.post<CompraRequestModel>(`${this.apiCompra}/inserir`, compra);
  }

  postUnirCompra(compra: CompraResponseModel, ideCompra: number) {
    return this.http.post<CompraRequestModel>(`${this.apiCompra}/unir/${ideCompra}`, compra);
  }
}
