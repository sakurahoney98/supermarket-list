import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastMessageSucess } from './toast-message-sucess';

describe('ToastMessageSucess', () => {
  let component: ToastMessageSucess;
  let fixture: ComponentFixture<ToastMessageSucess>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastMessageSucess],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastMessageSucess);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
