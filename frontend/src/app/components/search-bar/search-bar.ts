import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  imports: [FormsModule],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
})
export class SearchBar {
  termoBuscado: string = '';
  @Output() busca: EventEmitter<string> = new EventEmitter<string>();

  onSearch() {
    
    this.busca.emit(this.termoBuscado);
  }
}
