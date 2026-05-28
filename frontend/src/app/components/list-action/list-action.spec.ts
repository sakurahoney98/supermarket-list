import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi } from 'vitest';

import { ListActionComponent } from './list-action';

describe('ListActionComponent', () => {
  let component: ListActionComponent;
  let fixture: ComponentFixture<ListActionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListActionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ListActionComponent);
    component = fixture.componentInstance;
    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deve receber quantidadeSelecionada via @Input', () => {
    fixture.componentRef.setInput('quantidadeSelecionada', 5);

    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.quantidadeSelecionada).toBe(5);
    expect(valorDOM).toContain('5')
  });

  it('deve emitir o evento excluir ao chamar onExcluirClick()', () => {
    const emitSpy = vi.spyOn(component.excluir, 'emit');

    component.onExcluirClick()

    expect(emitSpy).toHaveBeenCalledOnce();
  });

  it('deve emitir true em selecionarTudo quando todosItensSelecionados for false', () => {
    component.todosItensSelecionados = false;
  const emitSpy = vi.spyOn(component.selecionarTudo, 'emit');

  component.toggleBotaoSelecionarTudoAtivado();

  expect(emitSpy).toHaveBeenCalledWith(true);
  expect(emitSpy).toHaveBeenCalledOnce();

  });

  it('deve emitir false em selecionarTudo quando todosItensSelecionados for true', () => {
   component.todosItensSelecionados = true;
  const emitSpy = vi.spyOn(component.selecionarTudo, 'emit');

  component.toggleBotaoSelecionarTudoAtivado();

  expect(emitSpy).toHaveBeenCalledWith(false);
  expect(emitSpy).toHaveBeenCalledOnce();

  });


});
