import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';

import { CategoriaModel } from '../models/categoria.model';
import { CategoriaRequestModel } from '../models/categoria-request.model';


@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private apiCategoria = '/api/categorias'


  constructor(private http: HttpClient) { }

  getCategorias() {
    return this.http.get<CategoriaModel[]>(this.apiCategoria + "/ordenar-por-nome");
  }

  deleteCategoria(ideCategoria: number) {
    return this.http.delete(this.apiCategoria + `/${ideCategoria}`);

  }

  deleteCategoriaEmMassa(ideCategoria: number[]) {
    return this.http.delete(this.apiCategoria, {
      body: ideCategoria
    });
  }

  inserirCategoria(categoria: CategoriaRequestModel) {
    return this.http.post(this.apiCategoria, categoria)
  }

  buscarTermo(termo: string) {
    const params = new HttpParams().set('nome', termo);

    return this.http.get<CategoriaModel[]>(`${this.apiCategoria}/filtro`,{ params });
  }
}
