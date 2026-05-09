import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-toast-message-success',
  imports: [],
  templateUrl: './toast-message-sucess.html',
  styleUrl: './toast-message-sucess.css',
})
export class ToastMessageSucessComponent {
  @Input() mensagem: string = '';
  @Input() mostrarToast: boolean = false;
  iconeSucesso = 'success-icon.png';
}
