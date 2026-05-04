import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-list',
  imports: [],
  templateUrl: './empty-list.html',
  styleUrl: './empty-list.css',
})
export class EmptyList {
  @Input() emoji: string = '😔';
  @Input() titulo: string = 'Lista vazia';
  @Input() descricao: string = 'Não há itens para exibir';
}
