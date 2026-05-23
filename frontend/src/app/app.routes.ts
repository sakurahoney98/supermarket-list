import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Categoria } from './pages/categoria/categoria';
import { Item } from './pages/item/item';
import { AtualizarEstoque } from './pages/atualizar-estoque/atualizar-estoque';
import { Lista } from './pages/lista/lista';
import { Compra } from './pages/compra/compra';
import { Relatorio } from './pages/relatorio/relatorio';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'categoria', component: Categoria},
    {path: 'item', component: Item},
    {path: 'atualizar-estoque', component: AtualizarEstoque},
    {path: 'lista-compras', component: Lista},
    {path: 'cadastrar-compra', component: Compra},
    {path: 'relatorios', component: Relatorio},
    { path: '**', redirectTo: '' }

];
 