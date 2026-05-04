import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-list-action',
  imports: [CommonModule],
  templateUrl: './list-action.html',
  styleUrl: './list-action.css',
})
export class ListAction {
  @Input() quantidadeSelecionada: number = 0
  @Input() todosItensSelecionados: boolean = false
  @Output() excluir = new EventEmitter<void>();
  @Output() selecionarTudo = new EventEmitter<boolean>();

  toggleBotaoSelecionarTudoAtivado(){
   
    this.selecionarTudo.emit(!this.todosItensSelecionados);

  }

  onExcluirClick(){
    this.excluir.emit();
  }
}
