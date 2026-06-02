import { TestBed } from '@angular/core/testing';

import { RelatorioService } from './relatorio';

describe('RelatorioService', () => {
  let service: RelatorioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RelatorioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
