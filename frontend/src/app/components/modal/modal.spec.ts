import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi } from 'vitest';

import { ModalComponent } from './modal';

describe('ModalComponent', () => {
  let component: ModalComponent;
  let fixture: ComponentFixture<ModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalComponent);
    component = fixture.componentInstance;
    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deve receber titulo via @Input', () => {
    fixture.componentRef.setInput('titulo', 'Título do modal');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.titulo).toBe('Título do modal');
    expect(valorDOM).toContain('Título do modal');
  })

  it('deve emitir o evento fecharModal ao chamar onClickFecharModal()', () => {
    const emitSpy = vi.spyOn(component.fecharModal, 'emit');

    component.onClickFecharModal();

    expect(emitSpy).toHaveBeenCalledOnce();
  });
});
