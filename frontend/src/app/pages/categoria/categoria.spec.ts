import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { Categoria } from './categoria';

import { CategoriaService } from '../../services/categoria';

import { CategoriaModel } from '../../models/categoria.model';
import { SugestaoModel } from '../../models/sugestao.model';
import { SugestaoService } from '../../services/sugestao';

describe('Categoria', () => {
  let component: Categoria;
  let fixture: ComponentFixture<Categoria>;

  const categoriaServiceMock = {
    getCategorias: vi.fn(),
    inserirCategoria: vi.fn(),
    deleteCategoria: vi.fn(),
    deleteCategoriaEmMassa: vi.fn(),
    buscarTermo: vi.fn(),
  };

  const sugestaoServiceMock = {
    getSugestoes: vi.fn(),
  }

  const catBebidas: CategoriaModel = {
    id: 1, nome: 'Bebidas', ativo: true, corLetra: '', corFundo: '', selecionado: false
  };

  const catLimpeza: CategoriaModel = {
    id: 2, nome: 'Limpeza', ativo: true, corLetra: '', corFundo: '', selecionado: false
  };

  const sugestao1: SugestaoModel = {
    nome: 'sugestão 1',
    corLetra: '#ffff',
    corFundo: '#000000'
  }

  const localStorageMock = {
    getItem: vi.fn(),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn(),
  };



  beforeEach(async () => {
    vi.clearAllMocks();

    await TestBed.configureTestingModule({
      imports: [Categoria],
      providers: [
        provideRouter([]),
        { provide: CategoriaService, useValue: categoriaServiceMock },
        { provide: SugestaoService, useValue: sugestaoServiceMock }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Categoria);
    component = fixture.componentInstance;

    localStorageMock.getItem.mockReturnValue(null);
    categoriaServiceMock.getCategorias.mockReturnValue(of([catBebidas, catLimpeza]));
    categoriaServiceMock.buscarTermo.mockReturnValue(of([catLimpeza]));
    categoriaServiceMock.inserirCategoria.mockReturnValue(of({}));
    categoriaServiceMock.deleteCategoria.mockReturnValue(of(void 0));
    categoriaServiceMock.deleteCategoriaEmMassa.mockReturnValue(of(void 0));
    sugestaoServiceMock.getSugestoes.mockReturnValue(of([sugestao1]));


  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInt deve carregar dados sem fazer HTTP real', () => {
    component.ngOnInit();

    expect(categoriaServiceMock.getCategorias).toHaveBeenCalledOnce();
    expect(sugestaoServiceMock.getSugestoes).toHaveBeenCalledOnce();

    expect(component.listaCategoria).toHaveLength(2);
    expect(component.listaSugestoes).toHaveLength(1);
  });

  it('deve validar os campos preenchidos quando cadastrarCategoria for chamado com preenchimento correto', () => {
    component.exemploTextoPreview = 'Teste Categoria';
    component.corFundo = '#53c212';
    component.corLetra = '#c0a612';

    component.cadastrarCategoria();

    expect(component.nomePreenchido).toBe(true);
    expect(component.corFundoPreenchido).toBe(true);
    expect(component.corLetraPreenchido).toBe(true);


  });

  it('deve rejeitar cadastro quandos os cmapos obrigatórios não forem preenchidos corretamente', () => {
    component.exemploTextoPreview = 'Teste Categoria';
    component.corFundo = '#53c212';
    component.corLetra = '';

    const exibirMensagemErroSpy = vi.spyOn(component, 'exibirMensagemDeErro');

    component.cadastrarCategoria();

    expect(component.nomePreenchido).toBe(true);
    expect(component.corFundoPreenchido).toBe(true);
    expect(component.corLetraPreenchido).toBe(false);

    expect(component.exibirToastErro).toBe(true);

    expect(exibirMensagemErroSpy).toHaveBeenCalledWith("Preencha os campos obrigatórios")


  });

  it('deve exibir mensagem de sucesso quando validação passa ao chamar cadastrarCategoria', () => {
    component.exemploTextoPreview = 'Teste Categoria';
    component.corFundo = '#53c212';
    component.corLetra = '#c0a612';

    
    const capturarCategoriasSpy = vi.spyOn(component, 'capturarCategorias').mockImplementation(() => { });

    component.cadastrarCategoria();

    expect(component.exibirToastSucesso).toBe(true);

    expect(categoriaServiceMock.inserirCategoria).toHaveBeenCalledWith({
      nome: 'Teste Categoria',
      corFundo: '#53c212',
      corLetra: '#c0a612',
    });
    expect(categoriaServiceMock.inserirCategoria).toHaveBeenCalledOnce();
    expect(capturarCategoriasSpy).toHaveBeenCalledOnce();


  });

  it('deve deletar uma categoria quando deletarCategoria for chamado', () => {
    component.listaCategoria = [
      { ...catBebidas, selecionado: true },
      { ...catLimpeza, selecionado: false }

    ];

    const exibirMensagemSucessoSpy = vi.spyOn(component, 'exibirMensagemDeSucesso');

    component.deletarCategoria();

    expect(component.listaCategoria).toHaveLength(1);

    expect(component.exibirToastSucesso).toBe(true);
    expect(exibirMensagemSucessoSpy).toHaveBeenCalledWith("Categoria deletada com sucesso!");

    expect(categoriaServiceMock.deleteCategoria).toHaveBeenCalledOnce();
    expect(categoriaServiceMock.deleteCategoria).toHaveBeenCalledWith(1);
  });

  it('deve deletar mais de uma categoria quando deletarCategoria for chamado', () => {
    component.listaCategoria = [
      { ...catBebidas, selecionado: true },
      { ...catLimpeza, selecionado: true }

    ];
    const exibirMensagemSucessoSpy = vi.spyOn(component, 'exibirMensagemDeSucesso');

    component.deletarCategoria();

    expect(component.listaCategoria).toHaveLength(0);

    expect(component.exibirToastSucesso).toBe(true);
    expect(exibirMensagemSucessoSpy).toHaveBeenCalledWith("Categorias deletadas com sucesso!");

    expect(categoriaServiceMock.deleteCategoriaEmMassa).toHaveBeenCalledOnce();
    expect(categoriaServiceMock.deleteCategoriaEmMassa).toHaveBeenCalledWith([1, 2]);
  });

  it('não deve deletar quando nenhuma categoria estiver selecionada ao chamar deletarCategoria', () => {
    component.listaCategoria = [
      { ...catBebidas, selecionado: false },
      { ...catLimpeza, selecionado: false }

    ];

    component.deletarCategoria();

    expect(component.listaCategoria).toHaveLength(2);

    expect(categoriaServiceMock.deleteCategoriaEmMassa).not.toHaveBeenCalled();
    
  });

  it('deve filtrar categoria por nome', () => {

    const termo: string = 'lImp';
    const atualizarQuantidadeSelecionadaSpy = vi.spyOn(component, 'atualizarQuantidadeSelecionada');

    component.onBusca(termo);

    expect(component.listaCategoria).toHaveLength(1);
    expect(component.listaCategoria[0].id).toBe(2);

    expect(categoriaServiceMock.buscarTermo).toHaveBeenCalledOnce();
    expect(categoriaServiceMock.buscarTermo).toHaveBeenCalledWith(termo);

    expect(atualizarQuantidadeSelecionadaSpy).toHaveBeenCalledOnce();



  });

  it('deve atualizar as informações de letra e cor de fundo de acordo com a sugetão selecionada', () => {
    component.selecionarSugestao(sugestao1);

    expect(component.corLetra).toBe('#ffff');
    expect(component.corFundo).toBe('#000000');

    expect(component.exibirModalSugestoes).toBe(false);

  });


});
