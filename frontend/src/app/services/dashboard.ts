import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/itens/dashboard`;

  constructor(private http: HttpClient){}

  getDashboard(){
    return this.http.get(this.apiUrl);
  }

}
