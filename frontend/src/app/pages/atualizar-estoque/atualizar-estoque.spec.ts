import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizarEstoque } from './atualizar-estoque';

describe('AtualizarEstoque', () => {
  let component: AtualizarEstoque;
  let fixture: ComponentFixture<AtualizarEstoque>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AtualizarEstoque],
    }).compileComponents();

    fixture = TestBed.createComponent(AtualizarEstoque);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
