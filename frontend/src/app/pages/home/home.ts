import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DashboardService } from '../../services/dashboard';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  standalone: true,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit{
  categorias = "-";
  itens = "-";
  itensZerados = "-";

  constructor (
    private dashboardService: DashboardService
  ){}

  ngOnInit(){

    this.dashboardService.getDashboard().subscribe({
      next: (data: any) => {

        this.categorias = String(data.categorias);
        this.itens = String(data.itens);
        this.itensZerados = String(data.itensZerados);

      },
      error: (err) => {
        console.log(err)
      }
    });

  }
}
