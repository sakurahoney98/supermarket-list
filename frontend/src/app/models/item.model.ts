import { CategoriaModel } from "./categoria.model";

export interface ItemModel {
    id: number,
	nome: string,
	unidadeMedida: string,
	quantidadeEstoque: number,
	limiteCompra: number,
	dataUltimaCompra: Date,
	categoria: CategoriaModel,
	duracaoDias: number,
	indAtivo: boolean,
	selecionado: boolean,
	novoValor: number
}