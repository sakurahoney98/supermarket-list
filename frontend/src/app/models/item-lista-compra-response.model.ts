import { CategoriaModel } from "./categoria.model";

export interface ItemListaCompraResponse{
   ideItem: number;
	nome: string;
	categoria: CategoriaModel;
	unidadeMedida: string;
	quantidadeCompra: number;
}