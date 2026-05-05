import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-page-title',
  imports: [],
  templateUrl: './page-title.html',
  styleUrl: './page-title.css',
})
export class PageTitleComponent {
  @Input() titulo: string = '';
  @Input() descricao: string = '';
}
