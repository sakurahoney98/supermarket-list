export interface ItemRequestModel {
    nome: string,
    unidadeMedida: string,
    quantidadeEstoque: number,
    limiteCompra: number,
    dataUltimaCompra: Date,
    categoria: number,
    duracaoDias: number
}