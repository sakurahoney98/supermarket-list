import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { of } from 'rxjs';

import { Home } from './home';
import { DashboardService } from '../../services/dashboard';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  const dashboardServiceMock = {
    getDashboard: vi.fn(),
  };

  const dashboard = { categorias: 10, itens: 20, itensZerados: 5 };

  beforeEach(async () => {

    dashboardServiceMock.getDashboard.mockReturnValue(of(dashboard));

    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [
        provideRouter([]),
        { provide: DashboardService, useValue: dashboardServiceMock },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit deve carregar dados sem fazer HTTP real', () => {
    component.ngOnInit();

    expect(component.categorias).toBe("10");
    expect(component.itens).toBe("20");
    expect(component.itensZerados).toBe("5");
  });
});
