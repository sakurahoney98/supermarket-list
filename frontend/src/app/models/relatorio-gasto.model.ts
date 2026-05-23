import { RelatorioGastoItemModel } from "./relatorio-gasto-item.model";

export interface RelatorioGastoModel {
    gastoTotal: number,
    historico: RelatorioGastoItemModel[]
}