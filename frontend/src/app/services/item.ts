import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';

import { ItemRequestModel } from '../models/item-request.model';
import { ItemModel } from '../models/item.model';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiItem = 'http://localhost:8080/itens'

  constructor(private http: HttpClient) { }

  getItens(){
    return this.http.get<ItemModel[]>(this.apiItem);
  }

  inserirItem(item: ItemRequestModel) {
    return this.http.post(this.apiItem, item)
  }

  editarItem  (item: ItemRequestModel, ideItem: number) {
    return this.http.put(this.apiItem + `/${ideItem}`, item)
  }

  deleteItem(ideItem: number) {
    return this.http.delete(this.apiItem + `/${ideItem}`);
  }

  deleteItemEmMassa(ideItem: number[]) {
    return this.http.delete(this.apiItem, {
      body: ideItem
    });
  }

  buscarItensPorCategoria (ideCategoria: number){

    const params = new HttpParams().set('ideCategoria', ideCategoria)

    return this.http.get<ItemModel[]>(this.apiItem, {params});
  }

  buscarItensPorNome(termo: string){
    const params = new HttpParams().set('nome', termo);

    return this.http.get<ItemModel[]>(this.apiItem, {params});
  }

  buscarItensPorNomeNaCategoria(termo: string, ideCategoria: number){
    let params = new HttpParams().set('nome', termo).set('ideCategoria', ideCategoria)

    return this.http.get<ItemModel[]>(this.apiItem, {params});

  }



  
}
