import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-list',
  imports: [],
  templateUrl: './empty-list.html',
  styleUrl: './empty-list.css',
})
export class EmptyListComponent {
  @Input() emoji: string = '😔';
  @Input() titulo: string = 'Nenhum resultado encontrado';
  @Input() descricao: string = 'Não há itens para exibir.';
}
