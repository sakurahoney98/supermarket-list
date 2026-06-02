import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageTitleComponent } from './page-title';

describe('PageTitleComponent', () => {
  let component: PageTitleComponent;
  let fixture: ComponentFixture<PageTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageTitleComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PageTitleComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deve receber titulo via @Input',() =>{
    fixture.componentRef.setInput('titulo', 'Título da página');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.titulo).toBe('Título da página');
    expect(valorDOM).toContain('Título da página');

  });

  it('deve receber descricao via @Input',() =>{
    fixture.componentRef.setInput('descricao', 'Descrição da página');
    fixture.detectChanges();

    const valorDOM: string = fixture.nativeElement.textContent ?? '';
    expect(component.descricao).toBe('Descrição da página');
    expect(valorDOM).toContain('Descrição da página');

  });
});
