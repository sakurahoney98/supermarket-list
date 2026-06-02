import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';

import { Relatorio } from './relatorio';
import { RelatorioService } from '../../services/relatorio';
import { ItemService } from '../../services/item';


vi.mock('xlsx', () => {
  const utils = {
    json_to_sheet: vi.fn(() => ({ mocked: 'sheet' })),
    aoa_to_sheet: vi.fn(() => ({ mocked: 'sheet' })),
    book_new: vi.fn(() => ({ mocked: 'workbook' })),
    book_append_sheet: vi.fn(),
  };

  return {
    utils,
    writeFile: vi.fn(),
  };
});

import * as XLSX from 'xlsx';

describe('Relatorio', () => {
  let component: Relatorio;
  let fixture: ComponentFixture<Relatorio>;

  const relatorioServiceMock = {
    getIntervaloAnosCompra: vi.fn(),
    getRelatorioMensal: vi.fn(),
    getRelatorioGasto: vi.fn(),
  };

  const itemServiceMock = {
    getItens: vi.fn(),
  };

  const item1 = () => ({
    id: 1,
    nome: 'Arroz',
  } as any);

  const item2 = () => ({
    id: 2,
    nome: 'Feijão',
  } as any);

  beforeEach(async () => {
    vi.clearAllMocks();

    vi.useFakeTimers();
    vi.setSystemTime(new Date('2026-06-02T10:00:00.000Z')); // 02/06/2026

    relatorioServiceMock.getIntervaloAnosCompra.mockReturnValue(
      of({ anoInicio: 2024, anoFim: 2026 })
    );
    itemServiceMock.getItens.mockReturnValue(of([item1(), item2()]));

    await TestBed.configureTestingModule({
      imports: [Relatorio],
      providers: [
        { provide: RelatorioService, useValue: relatorioServiceMock },
        { provide: ItemService, useValue: itemServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Relatorio);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar anos, itens e setar o título padrão', () => {
    component.ngOnInit();

    expect(relatorioServiceMock.getIntervaloAnosCompra).toHaveBeenCalledOnce();
    expect(itemServiceMock.getItens).toHaveBeenCalledOnce();

    expect(component.tituloRelatorio).toBe(component.tituloRelatorioCompra);


    expect(component.listaAnos).toStrictEqual([2024, 2025, 2026]);
    expect(component.anoSelecionado).toBe(2024);

    expect(component.listaItens).toHaveLength(2);
    expect(component.itemSelecionado.id).toBe(1);
  });

  it('capturarIntervaloDeAnosCompra deve adicionar apenas o ano atual quando anoInicio/anoFim for 0', () => {
    relatorioServiceMock.getIntervaloAnosCompra.mockReturnValue(
      of({ anoInicio: 0, anoFim: 0 })
    );

    component.capturarIntervaloDeAnosCompra();


    expect(component.listaAnos).toStrictEqual([2026]);
    expect(component.anoSelecionado).toBe(2026);
  });

  it('capturarItens deve setar listaItens e itemSelecionado com o primeiro item', () => {
    itemServiceMock.getItens.mockReturnValue(of([item2(), item1()]));

    component.capturarItens();

    expect(component.listaItens.map(i => i.id)).toStrictEqual([2, 1]);
    expect(component.itemSelecionado.id).toBe(2);
  });

  it('selecionarMes deve selecionar e fechar a lista de meses', () => {
    component.exibirMESES = true;

    component.selecionarMes({ numero: 12, nome: 'Dezembro' });

    expect(component.mesSelecionado).toStrictEqual({ numero: 12, nome: 'Dezembro' });
    expect(component.exibirMESES).toBe(false);
  });

  it('selecionarAno deve selecionar e fechar a lista de anos', () => {
    component.exibirListaAnos = true;

    component.selecionarAno(2030);

    expect(component.anoSelecionado).toBe(2030);
    expect(component.exibirListaAnos).toBe(false);
  });

  it('selecionarItem deve selecionar e fechar a lista de itens', () => {
    component.exibirListaItem = true;

    const it = item2();
    component.selecionarItem(it);

    expect(component.itemSelecionado).toStrictEqual(it);
    expect(component.exibirListaItem).toBe(false);
  });

  it('togglePerspectiva deve alternar flags e título', () => {

    expect(component.relatorioComprasAtivo).toBe(true);
    expect(component.relatorioGastosAtivo).toBe(false);

    component.togglePerspectiva();

    expect(component.relatorioComprasAtivo).toBe(false);
    expect(component.relatorioGastosAtivo).toBe(true);
    expect(component.tituloRelatorio).toBe(component.tituloRelatorioGasto);

    component.togglePerspectiva();

    expect(component.relatorioComprasAtivo).toBe(true);
    expect(component.relatorioGastosAtivo).toBe(false);
    expect(component.tituloRelatorio).toBe(component.tituloRelatorioCompra);
  });

  it('formatarDataBR deve formatar YYYY-MM-DD para DD/MM/YYYY', () => {
    expect(component.formatarDataBR('2026-06-02')).toBe('02/06/2026');
  });

  it('gerarRelatorioCompra deve exibir erro quando lista vier vazia', () => {
    relatorioServiceMock.getRelatorioMensal.mockReturnValue(of([]));

    const exibirErroSpy = vi
      .spyOn(component, 'exibirMensagemDeErro')
      .mockImplementation(() => {});

    component.gerarRelatorioCompra();

    expect(relatorioServiceMock.getRelatorioMensal).toHaveBeenCalledOnce();
    expect(exibirErroSpy).toHaveBeenCalledWith(
      'Não foram encontrados registros para os parâmetros selecionados.'
    );
  });

  it('gerarRelatorioCompra deve exportar planilha quando vier dados', () => {
    relatorioServiceMock.getRelatorioMensal.mockReturnValue(
      of([
        { nomeItem: 'Arroz', marca: 'Tio João', preco: 10, quantidade: 2 },
      ] as any)
    );

    const exportSpy = vi
      .spyOn(component, 'exportarPlanilhaCompra')
      .mockImplementation(() => {});

    component.gerarRelatorioCompra();

    expect(exportSpy).toHaveBeenCalledOnce();
  });

  it('gerarRelatorioCompra deve exibir erro quando service falhar', () => {
    relatorioServiceMock.getRelatorioMensal.mockReturnValue(
      throwError(() => ({ error: 'Falha no servidor' }))
    );

    const exibirErroSpy = vi
      .spyOn(component, 'exibirMensagemDeErro')
      .mockImplementation(() => {});

    component.gerarRelatorioCompra();

    expect(exibirErroSpy).toHaveBeenCalledWith('Falha no servidor');
  });

  it('gerarRelatorioGasto deve exibir erro quando gastoTotal for 0', () => {
    relatorioServiceMock.getRelatorioGasto.mockReturnValue(
      of({ gastoTotal: 0, historico: [] } as any)
    );

    const exibirErroSpy = vi
      .spyOn(component, 'exibirMensagemDeErro')
      .mockImplementation(() => {});

    component.gerarRelatorioGasto();

    expect(relatorioServiceMock.getRelatorioGasto).toHaveBeenCalledOnce();
    expect(exibirErroSpy).toHaveBeenCalledWith(
      'Não foram encontrados registros para os parâmetros selecionados.'
    );
  });

  it('gerarRelatorioGasto deve exportar planilha quando gastoTotal > 0', () => {
    relatorioServiceMock.getRelatorioGasto.mockReturnValue(
      of({
        gastoTotal: 123,
        historico: [{ dataCompra: '2026-06-01', marca: 'X', valorTotalPago: 50 }],
      } as any)
    );

    const exportSpy = vi
      .spyOn(component, 'exportarPlanilhaGasto')
      .mockImplementation(() => {});

    component.gerarRelatorioGasto();

    expect(exportSpy).toHaveBeenCalledOnce();
  });

  it('gerarRelatorioGasto deve exibir erro quando service falhar', () => {
    relatorioServiceMock.getRelatorioGasto.mockReturnValue(
      throwError(() => ({ error: 'Parâmetros inválidos' }))
    );

    const exibirErroSpy = vi
      .spyOn(component, 'exibirMensagemDeErro')
      .mockImplementation(() => {});

    component.gerarRelatorioGasto();

    expect(exibirErroSpy).toHaveBeenCalledWith('Parâmetros inválidos');
  });

  it('exportarPlanilhaCompra deve chamar XLSX.writeFile com nome de arquivo esperado', () => {
    component.anoSelecionado = 2026;
    component.mesSelecionado = { numero: 6, nome: 'Junho' };

    component.exportarPlanilhaCompra([
      { nomeItem: 'Arroz', marca: 'Tio João', preco: 10, quantidade: 2 },
    ] as any);

    expect(XLSX.utils.json_to_sheet).toHaveBeenCalledOnce();
    expect(XLSX.utils.book_new).toHaveBeenCalledOnce();
    expect(XLSX.utils.book_append_sheet).toHaveBeenCalledOnce();
    expect(XLSX.writeFile).toHaveBeenCalledOnce();

    const [, filename] = (XLSX.writeFile as any).mock.calls[0];
    expect(String(filename)).toContain('compras-6-2026_');
    expect(String(filename)).toContain('.xlsx');
  });

  it('exportarPlanilhaGasto deve chamar XLSX.writeFile com nome contendo o nome do item', () => {
    component.itemSelecionado = { id: 1, nome: 'Arroz' } as any;
    component.dataInicial = '2026-06-01';
    component.dataFinal = '2026-06-02';

    component.exportarPlanilhaGasto({
      gastoTotal: 100,
      historico: [{ dataCompra: '2026-06-01', marca: 'X', valorTotalPago: 50 }],
    } as any);

    expect(XLSX.utils.aoa_to_sheet).toHaveBeenCalledOnce();
    expect(XLSX.utils.book_new).toHaveBeenCalledOnce();
    expect(XLSX.utils.book_append_sheet).toHaveBeenCalledOnce();
    expect(XLSX.writeFile).toHaveBeenCalledOnce();

    const [, filename] = (XLSX.writeFile as any).mock.calls[0];
    expect(String(filename)).toContain('gastos-Arroz_');
    expect(String(filename)).toContain('.xlsx');
  });

 
});