import { CategoriaModel } from "./categoria.model";

export interface ItemListaCompraRequest{
    ideItem: number;
    nomeItem: string;
    unidadeMedida: string;
    categoria: CategoriaModel;
    quantidadeAtual: number;
    quantidadeSugerida: number;
	novoValor: number;
    limiteCompra: number;
}