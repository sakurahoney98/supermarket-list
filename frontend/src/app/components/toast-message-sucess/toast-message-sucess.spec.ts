import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastMessageSucessComponent } from './toast-message-sucess';

describe('ToastMessageSucessComponent', () => {
  let component: ToastMessageSucessComponent;
  let fixture: ComponentFixture<ToastMessageSucessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastMessageSucessComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastMessageSucessComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

   it('deve receber mensagem via @Input',() => {
    fixture.componentRef.setInput('mensagem', 'Mensagem de sucesso');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.mensagem).toBe('Mensagem de sucesso');
    expect(valorDOM).toContain('Mensagem de sucesso');

  });

  it('deve receber mostrarToast via @Input',() => {
    fixture.componentRef.setInput('mostrarToast', true);
    fixture.detectChanges();

    expect(component.mostrarToast).toBe(true);
    

  });
});
