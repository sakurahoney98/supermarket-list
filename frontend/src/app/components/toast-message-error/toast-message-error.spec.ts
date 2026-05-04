import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastMessageError } from './toast-message-error';

describe('ToastMessageError', () => {
  let component: ToastMessageError;
  let fixture: ComponentFixture<ToastMessageError>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastMessageError],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastMessageError);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
