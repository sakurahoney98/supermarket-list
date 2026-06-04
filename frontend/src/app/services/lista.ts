import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ItemListaCompraRequest } from '../models/item-lista-compra-request.model';
import { ItemListaCompraResponse } from '../models/item-lista-compra-response.model';

import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ListaService {

  private apiLista = `${environment.apiUrl}/lista-compras`

  constructor(
    private http: HttpClient
  ){}

  getLista(){
   return this.http.get<ItemListaCompraRequest[]>(this.apiLista)
  }

  postPDF(lista: ItemListaCompraResponse[]){
    return this.http.post(this.apiLista + '/pdf', lista, {
    responseType: 'blob'
  }); 

  }



}
