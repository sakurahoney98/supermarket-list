import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SugestaoModel } from '../models/sugestao.model';

@Injectable({
  providedIn: 'root',
})
export class SugestaoService {
  private apiSugestao = "http://localhost:8080/sugestao"

  constructor(private http: HttpClient){}

  getSugestoes(){
    return this.http.get<SugestaoModel[]>(this.apiSugestao);
  }
}
