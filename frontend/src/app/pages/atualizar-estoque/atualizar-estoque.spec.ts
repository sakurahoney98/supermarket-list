import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { AtualizarEstoque } from './atualizar-estoque';

import { ItemService } from '../../services/item';
import { CategoriaService } from '../../services/categoria';

import { ItemModel } from '../../models/item.model';
import { CategoriaModel } from '../../models/categoria.model';

describe('AtualizarEstoque', () => {
  let component: AtualizarEstoque;
  let fixture: ComponentFixture<AtualizarEstoque>;

  const itemServiceMock = {
      getItensEstoque: vi.fn(),
      atualizarEstoque: vi.fn(),
    };

    const categoriaServiceMock = {
      getCategorias: vi.fn(),
    };

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
    categoriaServiceMock.getCategorias.mockReturnValue(of([catBebidas, catLimpeza]));
    itemServiceMock.getItensEstoque.mockReturnValue(of([item1(), item2(), item3()]));
    itemServiceMock.atualizarEstoque.mockReturnValue(of(void 0));

    await TestBed.configureTestingModule({
      imports: [AtualizarEstoque],
      providers: [
        provideRouter([]),
        { provide: ItemService, useValue: itemServiceMock },
        { provide: CategoriaService, useValue: categoriaServiceMock },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AtualizarEstoque);
    component = fixture.componentInstance;
    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar dados sem fazer HTTP real', () => {
    component.ngOnInit();

    expect(categoriaServiceMock.getCategorias).toHaveBeenCalledOnce();
    expect(itemServiceMock.getItensEstoque).toHaveBeenCalledOnce();

    expect(component.listaItens).toHaveLength(3);
    expect(component.listaItens[0].novoValor).toBe(component.listaItens[0].quantidadeEstoque);

    expect(component.listaItensCopy).toHaveLength(3);

    expect(component.listaCategoriasExibicao).toHaveLength(2);
    
  });

  it('capturarItens deve aplicar rascunho do localStorage sobrescrevendo novoValor', () => {
    localStorageMock.getItem.mockReturnValue(
      JSON.stringify([{ id: 10, novoValor: 99 }])
    );

    component.capturarItens();

    expect(localStorage.getItem).toHaveBeenCalledWith('listaAtualizarEstoque');
    expect(itemServiceMock.getItensEstoque).toHaveBeenCalledOnce();

    const itemModificado = component.listaItens.find(i => i.id === 10)!;
    const itemNaoModificado = component.listaItens.find(i => i.id === 20)!;

    expect(itemModificado.novoValor).toBe(99);
    expect(itemModificado.novoValor).not.toBe(itemModificado.quantidadeEstoque);
    expect(itemNaoModificado.novoValor).toBe(itemNaoModificado.quantidadeEstoque)


  });

  it('capturarItens deve manter quantidadeEstoque da lista quando esta divergir da quantidadeEstoque do rascunho', () => {
    localStorageMock.getItem.mockReturnValue(
      JSON.stringify([{ id: 10, quantidadeEstoque: 2, novoValor: 2 }])
    );

    component.capturarItens();

    expect(localStorage.getItem).toHaveBeenCalledWith('listaAtualizarEstoque');
    expect(itemServiceMock.getItensEstoque).toHaveBeenCalledOnce();

    const itemModificado = component.listaItens.find(i => i.id === 10)!;
    const itemNaoModificado = component.listaItens.find(i => i.id === 20)!;

    expect(itemModificado.novoValor).toBe(3);
    expect(itemModificado.novoValor).toBe(itemModificado.quantidadeEstoque);
    expect(itemNaoModificado.novoValor).toBe(itemNaoModificado.quantidadeEstoque)


  });

  it('capturarItens deve aplicar rascunho do localStorage sobrescrevendo novoValor quando rascunho tiver a quantidade modificada', () => {
    localStorageMock.getItem.mockReturnValue(
      JSON.stringify([{ id: 10, quantidadeEstoque: 2, novoValor: 99 }])
    );

    component.capturarItens();

    expect(localStorage.getItem).toHaveBeenCalledWith('listaAtualizarEstoque');
    expect(itemServiceMock.getItensEstoque).toHaveBeenCalledOnce();

    const itemModificado = component.listaItens.find(i => i.id === 10)!;
    const itemNaoModificado = component.listaItens.find(i => i.id === 20)!;

    expect(itemModificado.novoValor).toBe(99);
    expect(itemModificado.novoValor).not.toBe(itemModificado.quantidadeEstoque);
    expect(itemNaoModificado.novoValor).toBe(itemNaoModificado.quantidadeEstoque)


  });

  it('onBusca deve filtrar itens por nome', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

    component.categoriaSelecionada = component.categoriaFiltro;
    const termo: string = 'cO';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);

    expect(component.listaItensCopy).toHaveLength(2);
    expect(component.listaItensCopy.map(i => i.id)).toEqual([10, 30]);
  


  });

  it('onBusca deve filtrar itens por categoria', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

    component.categoriaSelecionada = catBebidas;

    component.onBusca();

    expect(component.listaItensCopy).toHaveLength(2);
    expect(component.listaItensCopy.map(i => i.id)).toEqual([10, 30]);
  
  });

   it('onBusca deve filtrar itens por categoria e nome', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

    component.categoriaSelecionada = catBebidas;
    const termo: string = 'Coca';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);

    expect(component.listaItensCopy).toHaveLength(1);
    expect(component.listaItensCopy[0].id).toBe(10);

    expect(component.listaCategoriasExibicao).toHaveLength(1);
    expect(component.listaCategoriasExibicao[0].id).toBe(catBebidas.id);


  });

  it('onBusca deve retornar todos os itens', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

    component.categoriaSelecionada = component.categoriaFiltro;
    const termo: string = '';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);
    
    expect(component.listaItensCopy).toHaveLength(3);
    expect(component.listaItensCopy.map(i => i.id)).toEqual([10, 20, 30]);

    expect(component.listaCategoriasExibicao).toHaveLength(2);
    expect(component.listaCategoriasExibicao.map(i => i.id)).toEqual([1, 2]);
    


  });

  

  it('listaItens deve ser salva no localStorage quando salvarRascunho for chamado', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

  component.salvarRascunho();

  expect(localStorageMock.setItem).toHaveBeenCalledOnce();
  expect(localStorageMock.setItem).toHaveBeenCalledWith(
    'listaAtualizarEstoque',
    JSON.stringify(component.listaItens)
  );
  
  });

   it('deve enviar apenas itens alterados quando salvarAlteracoes for chamado', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 99})
  ];

  component.salvarAlteracoes();

  expect(itemServiceMock.atualizarEstoque).toHaveBeenCalledOnce();
  expect(itemServiceMock.atualizarEstoque).toHaveBeenCalledWith([
    {
      ideItem: 30,          
      quantidadeAtual: 4,
      quantidadeNova: 99,
    },
  ]);
 
 
  });

  it('envia uma lista vazia quando não há alterações no estoque e salvarAlteracoes for chamado', () => {
    component.listaItens = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

  component.salvarAlteracoes();

  expect(itemServiceMock.atualizarEstoque).toHaveBeenCalledOnce();
  expect(itemServiceMock.atualizarEstoque).toHaveBeenCalledWith([]);
  
 
  });

  it('deve alterar a categoria selecionada quando toggleSelecionarCategoria for chamado', () => {
    expect(component.categoriaSelecionada).toBe(component.categoriaFiltro);
    const onBuscaSpy = vi
    .spyOn(component, 'onBusca')
    .mockImplementation(() => {}); 

    component.toggleSelecionarCategoriaFiltro(catBebidas);
    

    expect(component.categoriaSelecionada).toBe(catBebidas);

    expect(component.exibirFiltro).toBe(false);

    expect(onBuscaSpy).toHaveBeenCalledOnce();


  });

  it('isAlteracaoDetectada deve retornar true quando novoValor !== quantidadeEstoque', () => {
    component.listaItens = [
      item1({ novoValor: 2 }),
      item2({ novoValor: 1 }),
      item3({ novoValor: 4})
    ];

    expect(component.isAlteracaoDetectada()).toBe(true);
  });

  it('deve acrescer 1 do novoValor do item quando aumentarValor for chamado', () => {

    const principal = item1({ quantidadeEstoque: 3, novoValor: 3 });
    component.listaItens = [principal];

    const vindoDoFiltro = { ...principal, novoValor: 3 };

    component.aumentarValor(vindoDoFiltro);

    expect(component.listaItens[0].novoValor).toBe(4);
    expect(vindoDoFiltro.novoValor).toBe(4);
    expect(component.estoqueModificado).toBe(true);

  });

  it('deve decrescer 1 do novoValor do item quando diminuirValor for chamado', () => {
    const principal = item1({ quantidadeEstoque: 3, novoValor: 3 });
    component.listaItens = [principal];

    const vindoDoFiltro = { ...principal, novoValor: 3 };

    component.diminuirValor(vindoDoFiltro);

    expect(component.listaItens[0].novoValor).toBe(2);
    expect(vindoDoFiltro.novoValor).toBe(2);
    expect(component.estoqueModificado).toBe(true);

  });

  it('não deve decrescer nada do novoValor do item quando diminuirValor for chamado', () => {
    const principal = item1({ quantidadeEstoque: 0, novoValor: 0 });
    component.listaItens = [principal];

    const vindoDoFiltro = { ...principal, novoValor: 0 };

    component.diminuirValor(vindoDoFiltro);

    expect(component.listaItens[0].novoValor).toBe(0);
    expect(vindoDoFiltro.novoValor).toBe(0);
    expect(component.estoqueModificado).toBe(false);

  });

  it('deve substituir o novoValor do item por quantidadeEstoque quando resetarValor for chamado', () => {
    const principal = item1({ quantidadeEstoque: 3, novoValor: 4 });
    component.listaItens = [principal];

    const vindoDoFiltro = { ...principal, novoValor: 4 };

    component.resetarValor(vindoDoFiltro);

    expect(component.listaItens[0].novoValor).toBe(3);

    expect(vindoDoFiltro.novoValor).toBe(3);

    expect(component.estoqueModificado).toBe(false);

  });

  it('deve substituir o novoValor de todos os itens por quantidadeEstoque quando resetarTudo for chamado', () => {
    const principal1 = item1({ quantidadeEstoque: 3, novoValor: 4 });
    const principal2 = item2({ quantidadeEstoque: 5, novoValor: 6 });
    component.listaItens = [principal1, principal2];

    component.resetarTudo();

    expect(component.listaItens.map(i => i.novoValor)).toEqual([3, 5]);

    expect(component.listaItensCopy.map(i => i.novoValor)).toEqual([3, 5]);

    expect(component.estoqueModificado).toBe(false);

    expect(localStorageMock.removeItem).toHaveBeenCalledOnce();
    expect(localStorageMock.removeItem).toHaveBeenCalledWith('listaAtualizarEstoque');

  });



  

});
