import { ItemCompraResponseModel } from "./item-compra-response.model";

export interface CompraResponseModel {
      dataCompra: Date,
    listaItens: ItemCompraResponseModel[]
}