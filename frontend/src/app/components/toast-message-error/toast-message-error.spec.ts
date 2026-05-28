import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastMessageErrorComponent } from './toast-message-error';

describe('ToastMessageErrorComponent', () => {
  let component: ToastMessageErrorComponent;
  let fixture: ComponentFixture<ToastMessageErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastMessageErrorComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastMessageErrorComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deve receber tituloErro via @Input',() => {
    fixture.componentRef.setInput('tituloErro', 'Título do erro');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.tituloErro).toBe('Título do erro');
    expect(valorDOM).toContain('Título do erro');

  });

  it('deve receber mensagem via @Input',() => {
    fixture.componentRef.setInput('mensagem', 'Mensagem do erro');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.mensagem).toBe('Mensagem do erro');
    expect(valorDOM).toContain('Mensagem do erro');

  });

  it('deve receber mostrarToast via @Input',() => {
    fixture.componentRef.setInput('mostrarToast', true);
    fixture.detectChanges();

    expect(component.mostrarToast).toBe(true);
    

  });
});
