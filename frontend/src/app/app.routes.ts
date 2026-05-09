import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Categoria } from './pages/categoria/categoria';
import { Item } from './pages/item/item';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'categoria', component: Categoria},
    {path: 'item', component: Item},
    { path: '**', redirectTo: '' }

];
 