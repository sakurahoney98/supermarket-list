import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-toast-message-sucess',
  imports: [],
  templateUrl: './toast-message-sucess.html',
  styleUrl: './toast-message-sucess.css',
})
export class ToastMessageSucess {
  @Input() mensagem: string = '';
  @Input() mostrarToast: boolean = false;
  iconeSucesso = 'success-icon.png';
}
