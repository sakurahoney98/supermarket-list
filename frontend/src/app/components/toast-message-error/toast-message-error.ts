import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-toast-message-error',
  imports: [],
  templateUrl: './toast-message-error.html',
  styleUrl: './toast-message-error.css',
})
export class ToastMessageError {
  @Input() tituloErro: string = 'Erro';
   @Input() mensagem: string = 'Mensagem de erro!';
  @Input() mostrarToast: boolean = true;
  iconeErro = 'warning-icon.png';
}
