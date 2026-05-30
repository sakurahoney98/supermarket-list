import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { Compra } from './compra';

import { ItemService } from '../../services/item';
import { CompraService } from '../../services/compra';

import { CategoriaModel } from '../../models/categoria.model';
import { ItemModel } from '../../models/item.model';
import { ItemCompraResponseModel } from '../../models/item-compra-response.model';

describe('Compra', () => {
  let component: Compra;
  let fixture: ComponentFixture<Compra>;

  const itemServiceMock = {
    getItens: vi.fn(),
    buscarItensPorNome: vi.fn(),
  };

  const compraServiceMock = {
    getCapturarComprasNaData: vi.fn(),
    postInserirCompra: vi.fn(),
    postUnirCompra: vi.fn(),

  }


  const catBebidas: CategoriaModel = {
    id: 1, nome: 'Bebidas', ativo: true, corLetra: '', corFundo: '', selecionado: false
  };

  const catLimpeza: CategoriaModel = {
    id: 2, nome: 'Limpeza', ativo: true, corLetra: '', corFundo: '', selecionado: false
  };

  const item1 = (override: Partial<ItemModel> = {}): ItemModel =>
  ({
    id: 10,
    nome: 'Coca-Cola',
    quantidadeEstoque: 3,
    categoria: catBebidas,
    ...override,
  } as ItemModel);

  const item2 = (override: Partial<ItemModel> = {}): ItemModel =>
  ({
    id: 20,
    nome: 'Detergente',
    quantidadeEstoque: 1,
    categoria: catLimpeza,
    ...override,
  } as ItemModel);

  const item3 = (override: Partial<ItemModel> = {}): ItemModel =>
  ({
    id: 30,
    nome: 'Suco',
    quantidadeEstoque: 4,
    categoria: catBebidas,
    ...override,
  } as ItemModel);


  const localStorageMock = {
    getItem: vi.fn(),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn(),
  };

  beforeEach(async () => {
    vi.clearAllMocks();

    Object.defineProperty(window, 'localStorage', {
      value: localStorageMock,
      writable: true,
      configurable: true,
    });

    localStorageMock.getItem.mockReturnValue(null);
    itemServiceMock.getItens.mockReturnValue(of([item1(), item2(), item3()]));
    compraServiceMock.postInserirCompra.mockReturnValue(null);
    compraServiceMock.postUnirCompra.mockReturnValue(null);
    compraServiceMock.getCapturarComprasNaData.mockReturnValue(null);

    await TestBed.configureTestingModule({
      imports: [Compra],
      providers: [
        provideRouter([]),
        { provide: ItemService, useValue: itemServiceMock },
        { provide: CompraService, useValue: compraServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Compra);
    component = fixture.componentInstance;


  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar dados sem fazer HTTP real', () => {
    const montarListaItensCompradosSpy = vi.spyOn(component, 'montarListaItensComprados').mockImplementation(() => { });

    component.ngOnInit();

    expect(itemServiceMock.getItens).toHaveBeenCalledOnce();
    expect(component.listaItens).toHaveLength(3);
    expect(montarListaItensCompradosSpy).toHaveBeenCalledOnce();


  });

  it('deve exibir lista vazia quando não houver rascunho ao chamar monstarListaItensComprados', () => {

    const calcularTotalSpy = vi.spyOn(component, 'calcularTotal').mockImplementation(() => { });

    component.montarListaItensComprados();

    expect(component.itensNaCompra).toHaveLength(0);
    expect(calcularTotalSpy).toHaveBeenCalledOnce();

  });

  it('deve montar a lista de compras quando houver rascunho ao chamar montarListaItensComprados', () => {
    localStorageMock.getItem.mockReturnValue(JSON.stringify([{
      id: 1,
      ideItem: 10,
      nomeItem: 'Coca-Cola',
      quantidadeComprada: 4,
      valor: 7.89,
      marca: 'Coca-Cola',
      subtotal: 7.89 * 4
    }])
    );
    component.listaItens = [item1(), item2(), item3()];

    const calcularTotalSpy = vi.spyOn(component, 'calcularTotal').mockImplementation(() => { });

    component.montarListaItensComprados();

    expect(localStorageMock.getItem).toHaveBeenCalledWith('listaInserirCompra');
    expect(localStorageMock.getItem).toHaveBeenCalled();
    expect(component.itensNaCompra).toHaveLength(1);

    expect(calcularTotalSpy).toHaveBeenCalledOnce();
  });

  it('deve inserir item na lista de compras quando inserirItemNaLista for chamado', () => {
    const calcularTotalSpy = vi.spyOn(component, 'calcularTotal').mockImplementation(() => { });

    component.inserirItemNaLista(item1());

    expect(component.itensNaCompra).toHaveLength(1);
    expect(component.itensNaCompra[0].id).toBe(1);
    expect(component.termoBuscado).toBe('');
    expect(component.exibirFiltro).toBe(false);

    expect(calcularTotalSpy).toHaveBeenCalledOnce();

  });

  it('deve remover item da lista de compras quando removerDaLista for chamado', () => {
    const calcularTotalSpy = vi.spyOn(component, 'calcularTotal').mockImplementation(() => { });

    component.inserirItemNaLista(item1());
    expect(component.itensNaCompra).toHaveLength(1);
    expect(calcularTotalSpy).toHaveBeenCalledOnce();

    calcularTotalSpy.mockClear();

    component.removerDaLista(component.itensNaCompra[0]);
    expect(component.itensNaCompra).toHaveLength(0);
    expect(calcularTotalSpy).toHaveBeenCalledOnce();

  });

  it('deve chamar o método inserirCompra quando não houver compras para a data ao chamar finalizar', () => {
    compraServiceMock.getCapturarComprasNaData.mockReturnValue(of([]));
    const inserirCompraSpy = vi.spyOn(component, 'inserirCompra').mockImplementation(() => { });


    component.finalizar();

    expect(component.exibirModal).toBe(false);
    expect(inserirCompraSpy).toHaveBeenCalledOnce();


  });

  it('deve abrir modal quando houver compras para a data ao chamar finalizar', () => {
    compraServiceMock.getCapturarComprasNaData.mockReturnValue(of([
      { ideCompra: 1, quantidadeItens: 15 }
    ]));

    component.finalizar();

    expect(component.exibirModal).toBe(true);
    expect(component.listaOpcoesConflito[1].ideCompra).toBe(1);

  });

  it('deve chamar método inserirCompra quando essa for a opção selecionada ao chamar reolverConflito', () => {
    component.opcaoConflitoSelecionada = component.opcaoConflito;

    const inserirCompraSpy = vi.spyOn(component, 'inserirCompra').mockImplementation(() => { });

    component.resolverConflito();

    expect(inserirCompraSpy).toHaveBeenCalled();
  });

  it('deve chamar o método unirCompra quando essa for a opção selecionada ao chamar reolverConflito', () => {
    component.opcaoConflitoSelecionada = {
      ideCompra: 1,
      quantidadeItens: 15
    };

    const unirCompraSpy = vi.spyOn(component, 'unirCompra').mockImplementation(() => { });

    component.resolverConflito();

    expect(unirCompraSpy).toHaveBeenCalled();
  });

  it('deve montar o objeto compra quando montarCompra for chamado', () => {
    component.dataCompra = '2026-05-04';
    component.inserirItemNaLista(item1());
    const dataConvertida = component.converterStringParaData('2026-05-04');

    const response = component.montarCompra();

    expect(response.dataCompra).toStrictEqual(dataConvertida);
    expect(response.listaItens.map(i => i.ideItem)).toStrictEqual([10]);
  });

  it('deve inserir uma nova compra quando inserirCompra for chamado', () => {
    component.dataCompra = '2026-04-05';
    compraServiceMock.postInserirCompra.mockReturnValue(of([
      { ideCompra: 1, quantidadeItens: 15 }
    ]));

    const exibirMensagemDeSucessoSpy = vi.spyOn(component, 'exibirMensagemDeSucesso');
    const resetarTudoSpy = vi.spyOn(component, 'resetarTudo');

    component.inserirCompra();

    expect(compraServiceMock.postInserirCompra).toHaveBeenCalledOnce();
    expect(exibirMensagemDeSucessoSpy).toHaveBeenCalledOnce();
    expect(exibirMensagemDeSucessoSpy).toHaveBeenCalledWith("Compra inserida com sucesso!");
    expect(resetarTudoSpy).toHaveBeenCalledOnce();
  });

  it('deve unir compra quando unirCompra for chamado', () => {
    component.dataCompra = '2026-04-05';
    component.opcaoConflitoSelecionada = { ideCompra: 1, quantidadeItens: 15 }
    compraServiceMock.postUnirCompra.mockReturnValue(of([
      { ideCompra: 2, quantidadeItens: 16 }
    ]));

    const exibirMensagemDeSucessoSpy = vi.spyOn(component, 'exibirMensagemDeSucesso');
    const resetarTudoSpy = vi.spyOn(component, 'resetarTudo');

    component.unirCompra();

    expect(component.exibirListaResolverConflito).toBe(false);
    expect(compraServiceMock.postUnirCompra).toHaveBeenCalledOnce();
    expect(compraServiceMock.postUnirCompra).toHaveBeenCalledWith({ "dataCompra": component.converterStringParaData(component.dataCompra), "listaItens": [] }, 1);
    expect(exibirMensagemDeSucessoSpy).toHaveBeenCalledOnce();
    expect(exibirMensagemDeSucessoSpy).toHaveBeenCalledWith("Compra inserida com sucesso!");
    expect(resetarTudoSpy).toHaveBeenCalledOnce();
  });

  it('deve salvar o rascunho no localStroage quando salvarRascunho for chamado', () => {

    component.salvarRascunho();

    expect(localStorageMock.setItem).toHaveBeenCalled();
  });

  it('deve retornar o estado incial da tela quando resetarTudo for chamado', () => {
    component.opcaoConflitoSelecionada = { ideCompra: 1, quantidadeItens: 15 };
    component.listaOpcoesConflito.push(component.opcaoConflito);
    component.inserirItemNaLista(item1());

    expect(component.totalCompra).not.toBe(0);

    component.resetarTudo();

    expect(component.itensNaCompra).toHaveLength(0);
    expect(component.totalCompra).toBe(0);
    expect(component.listaOpcoesConflito).toHaveLength(0);
    expect(component.opcaoConflitoSelecionada).toBe(component.opcaoConflito);
    expect(localStorageMock.removeItem).toHaveBeenCalled();

  });

  it('deve retornar resultado da busca quando o termo buscado for diferente de vazio ao chamar onBusca', () => {
    component.termoBuscado = 'cOc';
    itemServiceMock.buscarItensPorNome.mockReturnValue(of([item1()]));
    component.listaItens = [];

    component.onBusca();

    expect(component.listaItens).toHaveLength(1);
    expect(component.exibirFiltro).toBe(true);
  });

  it('deve retornar todos os itens quando o termo buscado for vazio ao chamar onBusca', () => {
    component.termoBuscado = '';
    component.listaItens = [];

    component.onBusca();

    expect(component.listaItens).toHaveLength(3);
    expect(component.exibirFiltro).toBe(false);
  });


});
