import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class ModalComponent {
  @Input() titulo: string = "-"
  @Output() fecharModal = new EventEmitter<void>;

  onClickFecharModal(){
    this.fecharModal.emit();

  }
}
