import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/dashboard';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit{
  categorias = "-";
  itens = "-";
  itensZerados = "-";

  constructor (
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ){}

  ngOnInit(){
    this.dashboardService.getDashboard().subscribe({
      next: (data: any) => {

        
        this.categorias = String(data.categorias);
        this.itens = String(data.itens);
        this.itensZerados = String(data.itensZerados);

        this.cdr.detectChanges();

      },
      error: (err) => {
        console.log(err)
      }
    });

  }
}
