import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListAction } from './list-action';

describe('ListAction', () => {
  let component: ListAction;
  let fixture: ComponentFixture<ListAction>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListAction],
    }).compileComponents();

    fixture = TestBed.createComponent(ListAction);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
