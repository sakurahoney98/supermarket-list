import { ComponentFixture, TestBed } from '@angular/core/testing';
import { vi } from 'vitest';

import { SearchBarComponent } from './search-bar';

describe('SearchBarComponent', () => {
  let component: SearchBarComponent;
  let fixture: ComponentFixture<SearchBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchBarComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchBarComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('emitir evento busca ao chamar onSearch()', () => {
    const emitSpy = vi.spyOn(component.busca, 'emit');

    component.onSearch();

    expect(emitSpy).toHaveBeenCalledOnce();
  })
});
