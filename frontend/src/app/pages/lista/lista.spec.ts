import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { Lista } from './lista';

import { ListaService } from '../../services/lista';
import { CategoriaService } from '../../services/categoria';

import { CategoriaModel } from '../../models/categoria.model';
import { ItemListaCompraRequest } from '../../models/item-lista-compra-request.model';



describe('Lista', () => {
  let component: Lista;
  let fixture: ComponentFixture<Lista>;

   const listaServiceMock = {
    getLista: vi.fn(),
    postPDF: vi.fn(),

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
    
      const item1 = (override: Partial<ItemListaCompraRequest> = {}): ItemListaCompraRequest =>
        ({
          ideItem: 10,
          nomeItem: 'Coca-Cola',
          quantidadeAtual: 0,
          categoria: catBebidas,
          quantidadeSugerida: 1,
          ...override,
        } as ItemListaCompraRequest);
    
      const item2 = (override: Partial<ItemListaCompraRequest> = {}): ItemListaCompraRequest =>
        ({
          ideItem: 20,
          nomeItem: 'Detergente',
          quantidadeAtual: 1,
          categoria: catLimpeza,
          quantidadeSugerida: 5,
          ...override,
        } as ItemListaCompraRequest);
    
         const item3 = (override: Partial<ItemListaCompraRequest> = {}): ItemListaCompraRequest =>
        ({
          ideItem: 30,
          nomeItem: 'Suco',
          quantidadeAtual: 1,
          categoria: catBebidas,
          quantidadeSugerida: 2,
          ...override,
        } as ItemListaCompraRequest);
    
    
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

     categoriaServiceMock.getCategorias.mockReturnValue(of([catBebidas, catLimpeza]));
     listaServiceMock.getLista.mockReturnValue(of([item1(), item2(), item3()]));
  
    await TestBed.configureTestingModule({
      imports: [Lista],
      providers: [
              provideRouter([]),
              { provide: ListaService, useValue: listaServiceMock },
              { provide: CategoriaService, useValue: categoriaServiceMock },
            ]
    }).compileComponents();

    

    fixture = TestBed.createComponent(Lista);
    component = fixture.componentInstance;
  
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar dados sem fazer HTTP real', () => {
    component.ngOnInit();

    expect(categoriaServiceMock.getCategorias).toHaveBeenCalledOnce();
    expect(component.listaCategoriasFiltro).toHaveLength(3);
  
    expect(listaServiceMock.getLista).toHaveBeenCalledOnce();
    expect(component.listaItensCompra).toHaveLength(3);
    expect(component.listaItensCompraCopy).toHaveLength(3);

    expect(component.estoqueModificado).toBe(false);
    expect(component.itensNaLista).toBe(true);
    
  });

  it('deve enviar a lista final para geração do pdf quando capturarPDF for chamado', () => {
    const blobMock = new Blob(['pdf content']);
    listaServiceMock.postPDF.mockReturnValue(of(blobMock));
    item1({ novoValor: 1 });

    component.listaItensCompra = [item1()];

    component.capturarPDF();

    expect(listaServiceMock.postPDF).toHaveBeenCalledOnce();
    expect(listaServiceMock.postPDF).toHaveBeenCalledWith([{
      ideItem: item1().ideItem,
        nome: item1().nomeItem,
        categoria: item1().categoria,
        unidadeMedida: item1().unidadeMedida,
        quantidadeCompra: item1().novoValor

    }]);
   
  });

   it('deve filtrar itens por nome quando onBusca for chamado', () => {
    component.listaItensCompra = [
    item1(),
    item2(),
    item3()
  ];

    component.categoriaSelecionada = component.categoriaFiltro;
    const termo: string = 'cO';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);
    expect(component.listaItensCompraCopy).toHaveLength(2);
    expect(component.listaItensCompraCopy.map(i => i.ideItem)).toEqual([10, 30]);
  

  });

  it('deve filtrar itens pela categoria quando onBusca for chamado', () => {
    component.listaItensCompra = [
    item1({ novoValor: 3 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 4})
  ];

    component.categoriaSelecionada = catBebidas;

    component.onBusca();

    expect(component.listaItensCompraCopy).toHaveLength(2);
    expect(component.listaItensCompraCopy.map(i => i.ideItem)).toEqual([10, 30]);
  
  });

   it('deve filtrar por nome e categoria quando onBusca for chamado', () => {
    component.listaItensCompra = [
    item1(),
    item2(),
    item3()
  ];

    component.categoriaSelecionada = catBebidas;
    const termo: string = 'CoCa';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);

    expect(component.listaItensCompraCopy).toHaveLength(1);
    expect(component.listaItensCompraCopy[0].ideItem).toBe(10);

    expect(component.listaCategoriasExibicao).toHaveLength(1);
    expect(component.listaCategoriasExibicao[0].id).toBe(catBebidas.id);


  });

  it('deve filtrar por itens que vão ser comprados quando onBusca for chamado', () => {
    component.listaItensCompra = [
    item1({ novoValor: 2 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 0})
  ];
    component.categoriaSelecionada = component.categoriaFiltro;
    component.exibirTodosItens = false;

    component.onBusca();

    expect(component.listaItensCompraCopy).toHaveLength(2);
    expect(component.listaItensCompraCopy.map(i => i.ideItem)).toEqual([10, 20]);
  
  });

  it('deve exibir todos os itens quando onBusca for chamado', () => {
    component.listaItensCompra = [
    item1({ novoValor: 2 }),
    item2({ novoValor: 1 }),
    item3({ novoValor: 0})
  ];
    component.categoriaSelecionada = component.categoriaFiltro;
    component.exibirTodosItens = true;

    component.onBusca();

    expect(component.listaItensCompraCopy).toHaveLength(3);
    expect(component.listaItensCompraCopy.map(i => i.ideItem)).toEqual([10, 20, 30]);
  
  });

  it('deve aumentar valor do item quando aumentarValor for chamado', () => {
    component.listaItensCompra = [item1()];
    const itemModificado = item1({ideItem: 10, quantidadeSugerida: 2, novoValor: 2});

    component.aumentarValor(itemModificado);

    expect(itemModificado.novoValor).toBe(3);
    expect(component.listaItensCompra.find(i => i.ideItem = item1().ideItem)?.novoValor).toBe(3);
  
  });

   it('deve diminuir valor do item quando diminuirValor for chamado', () => {
    component.listaItensCompra = [item1()];
    const itemModificado = item1({ideItem: 10, quantidadeSugerida: 2, novoValor: 2});

    component.diminuirValor(itemModificado);

    expect(itemModificado.novoValor).toBe(1);
    expect(component.listaItensCompra.find(i => i.ideItem = item1().ideItem)?.novoValor).toBe(1);
  
  });

  it('não deve diminuir valor do item quando diminuirValor for chamado', () => {
    component.listaItensCompra = [item1({ novoValor: 2 })];
    const itemModificado = item1({ideItem: 10, quantidadeSugerida: 2, novoValor: 0});

    component.diminuirValor(itemModificado);

    expect(itemModificado.novoValor).toBe(0);
    expect(component.listaItensCompra.find(i => i.ideItem = item1().ideItem)?.novoValor).toBe(2);
  
  });

   it('deve voltar o valor de quantidade sugerida para o item quando resetarValor for chamado', () => {
    component.listaItensCompra = [item1()];
    const itemModificado = item1({ideItem: 10, quantidadeSugerida: 2, novoValor: 5});

    component.resetarValor(itemModificado);

    expect(itemModificado.novoValor).toBe(2);
    expect(component.listaItensCompra.find(i => i.ideItem = item1().ideItem)?.novoValor).toBe(2);
  
  });

  it('deve voltar o valor de quantidade sugerida para todos os itens quando resetarTudo for chamado', () => {
    component.listaItensCompra = [
    item1({ novoValor: 7 }),
    item2({ novoValor: 8 }),
    item3({ novoValor: 9})
  ];
    component.resetarTudo();

    expect(component.listaItensCompra[0].novoValor).toBe(1);
    expect(component.listaItensCompraCopy[0].novoValor).toBe(1);

  
  });

  
});
