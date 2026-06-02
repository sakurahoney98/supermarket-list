import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { Item } from './item';

import { ItemService } from '../../services/item';
import { CategoriaService } from '../../services/categoria';

import { ItemModel } from '../../models/item.model';
import { CategoriaModel } from '../../models/categoria.model';



describe('Item', () => {
  let component: Item;
  let fixture: ComponentFixture<Item>;

  const itemServiceMock = {
    getItens: vi.fn(),
    inserirItem: vi.fn(),
    deleteItem: vi.fn(),
    deleteItemEmMassa: vi.fn(),
    editarItem: vi.fn(),
    buscarItensPorNomeNaCategoria: vi.fn(),
    buscarItensPorCategoria: vi.fn(),
    buscarItensPorNome: vi.fn(),
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

  beforeEach(async () => {
    vi.clearAllMocks();

    categoriaServiceMock.getCategorias.mockReturnValue(of([catBebidas, catLimpeza]));
    itemServiceMock.getItens.mockReturnValue(of([item1(), item2(), item3()]));
    itemServiceMock.inserirItem.mockReturnValue(of(void 0));
    itemServiceMock.deleteItem.mockReturnValue(of(void 0));
    itemServiceMock.deleteItemEmMassa.mockReturnValue(of(void 0));
    itemServiceMock.editarItem.mockReturnValue(of(void 0));


    await TestBed.configureTestingModule({
      imports: [Item],
      providers: [
        provideRouter([]),
        { provide: ItemService, useValue: itemServiceMock },
        { provide: CategoriaService, useValue: categoriaServiceMock },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Item);
    component = fixture.componentInstance;

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar dados sem fazer HTTP real', () => {
    component.ngOnInit();

    expect(itemServiceMock.getItens).toHaveBeenCalledOnce();
    expect(categoriaServiceMock.getCategorias).toHaveBeenCalledOnce();

    expect(component.listaItens).toHaveLength(3);

    expect(component.listaCategorias).toHaveLength(2);
    expect(component.listaCategoriasFiltro).toHaveLength(3);
    expect(component.categoriaCadastroSelecionada.id).toBe(1);
    expect(component.itemCadastro.categoria).toBe(1);


  });

    it('deve validar preenchimento quando todos os campos obrigatórios forem preenchidos', () => {
    const itemCadastro = {
      nome: 'Amaciante',
      unidadeMedida: '1l',
      quantidadeEstoque: 1,
      limiteCompra: 2,
      dataUltimaCompra: new Date(),
      categoria: 2,
      duracaoDias: 30
    };

    const response = component.isCamposObrigatoriosPreenchidos(itemCadastro);

    expect(response).toBe(true);

  });


  it('deve rejeitar preenchimento quando os campos obrigatórios não forem preenchidos corretamente', () => {
    const itemCadastro = {
      nome: '',
      unidadeMedida: '1l',
      quantidadeEstoque: 1,
      limiteCompra: 2,
      dataUltimaCompra: new Date(),
      categoria: 2,
      duracaoDias: 30
    };

    const response = component.isCamposObrigatoriosPreenchidos(itemCadastro);

    expect(response).toBe(false);

  });

  it('deve cadastrar item se todos os campos obrigatórios tiverem sido preenchidos ao chamar cadastrarItem', () => {
    const resetarEstadoInputSpy = vi.spyOn(component, 'resetarEstadoInput').mockImplementation(() => { });
    const isCamposObrigatoriosPreenchidosSpy = vi.spyOn(component, 'isCamposObrigatoriosPreenchidos').mockReturnValue(true);
    const exibirMensagemDeSucessoSpy = vi.spyOn(component, 'exibirMensagemDeSucesso');
    const capturarItensSpy = vi.spyOn(component, 'capturarItens').mockImplementation(() => { });
    component.itemCadastro = {
      nome: 'Amaciante',
      unidadeMedida: '1l',
      quantidadeEstoque: 1,
      limiteCompra: 2,
      dataUltimaCompra: new Date(),
      categoria: 2,
      duracaoDias: 30
    };
    component.listaCategorias = [catBebidas, catLimpeza];


    component.cadastrarItem();

    expect(resetarEstadoInputSpy).toHaveBeenCalled();
    expect(isCamposObrigatoriosPreenchidosSpy).toHaveBeenCalled();
    expect(exibirMensagemDeSucessoSpy).toHaveBeenCalledWith("Item cadastrado com sucesso");
    expect(capturarItensSpy).toHaveBeenCalled();
  });

  it('deve rejeitar cadastro do item se os campos obrigatórios não tiverem sido preenchidos corretamente ao chamar cadastrarItem', () => {

    const isCamposObrigatoriosPreenchidosSpy = vi.spyOn(component, 'isCamposObrigatoriosPreenchidos').mockReturnValue(false);
    const exibirMensagemDeErroSpy = vi.spyOn(component, 'exibirMensagemDeErro');

    component.cadastrarItem();

    expect(isCamposObrigatoriosPreenchidosSpy).toHaveBeenCalled();
    expect(exibirMensagemDeErroSpy).toHaveBeenCalledWith("Preencha todos os campos obrgatórios.");

  });

  it('deve deletar um item quando deletarItens for chamado', () => {
    component.listaItens = [
      item1({ selecionado: true }),
      item2({ selecionado: false }),
      item3({ selecionado: false })
    ];

    component.deletarItens();

    expect(component.listaItens).toHaveLength(2);
    expect(itemServiceMock.deleteItem).toHaveBeenCalledWith(10);
  });

  it('deve deletar  todos os itens selecionados quando deletarItens for chamado', () => {
    component.listaItens = [
      item1({ selecionado: true }),
      item2({ selecionado: true }),
      item3({ selecionado: false })
    ];

    component.deletarItens();

    expect(component.listaItens).toHaveLength(1);
    expect(itemServiceMock.deleteItemEmMassa).toHaveBeenCalledWith([10, 20]);
  });

  it('não deve deletar nenhum item deletarItens for chamado', () => {
    component.listaItens = [
      item1({ selecionado: false }),
      item2({ selecionado: false }),
      item3({ selecionado: false })
    ];

    component.deletarItens();

    expect(component.listaItens).toHaveLength(3);

  });

  it('deve salvar item editado quando preenchido corretamente ao chamar editarItem', () => {
    component.itemEdicao = item1();
    component.itemEdicao.nome = 'Novo nome';
    component.categoriaEdicaoSelecionada = item1().categoria;
    const isCamposObrigatoriosPreenchidosSpy = vi.spyOn(component, 'isCamposObrigatoriosPreenchidos').mockReturnValue(true);

    component.editarItem();

    expect(itemServiceMock.editarItem).toHaveBeenCalledOnce();
    expect(itemServiceMock.editarItem).toHaveBeenCalledWith(
      {
        nome: 'Novo nome',
        unidadeMedida: component.itemEdicao.unidadeMedida,
        quantidadeEstoque: component.itemEdicao.quantidadeEstoque,
        limiteCompra: component.itemEdicao.limiteCompra,
        dataUltimaCompra: component.itemEdicao.dataUltimaCompra,
        categoria: component.itemEdicao.categoria.id,
        duracaoDias: component.itemEdicao.duracaoDias,
      },
      10
    );
    expect(isCamposObrigatoriosPreenchidosSpy).toHaveBeenCalled();
  });


  it('deve filtrar itens por categoria quando onBusca for chamado', () => {
   component.categoriaSelecionada = catBebidas;
    itemServiceMock.buscarItensPorCategoria.mockReturnValue(of([]));

    component.onBusca();

    expect(component.termoBuscado).toBe('');
    expect(itemServiceMock.buscarItensPorCategoria).toHaveBeenCalledOnce();
    expect(itemServiceMock.buscarItensPorCategoria).toHaveBeenCalledWith(catBebidas.id);


  });

  it('deve buscar itens por nome quando onBusca for chamado', () => {
   component.categoriaSelecionada = component.categoriaFiltro;
    itemServiceMock.buscarItensPorNome.mockReturnValue(of([]));
    const termo: string = 'Termo buscado';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);
    expect(itemServiceMock.buscarItensPorNome).toHaveBeenCalledOnce();
    expect(itemServiceMock.buscarItensPorNome).toHaveBeenCalledWith(termo);

  });

  it('deve buscar por nome na categoria quando onBusca for chamado', () => {
    
    component.categoriaSelecionada = catBebidas;
    itemServiceMock.buscarItensPorNomeNaCategoria.mockReturnValue(of([]));
    const termo: string = 'Termo buscado';

    component.onBusca(termo);

    expect(component.termoBuscado).toBe(termo);
    expect(itemServiceMock.buscarItensPorNomeNaCategoria).toHaveBeenCalledTimes(1);
    expect(itemServiceMock.buscarItensPorNomeNaCategoria).toHaveBeenCalledWith(termo, catBebidas.id);
  
  });

  it('deve retornar todos os itens quando onBusca for chamado', () => {
   
    component.categoriaSelecionada = component.categoriaFiltro;
    const capturarItensSpy = vi.spyOn(component, 'capturarItens').mockImplementation(() => {});
    
    component.onBusca();

    expect(capturarItensSpy).toHaveBeenCalledOnce();

  });

  it('deve exibir mensagem de erro quando não houver categoria cadastrada ao chamar onAbrirModal', () => {
    component.listaCategorias = [];
    const exibirMensagemDeErroSpy = vi.spyOn(component, 'exibirMensagemDeErro');

    component.onAbrirModal();

    expect(exibirMensagemDeErroSpy).toHaveBeenCalledWith("Necessário cadastrar uma categoria antes de cadastrar item.");

  });


  


   
});
