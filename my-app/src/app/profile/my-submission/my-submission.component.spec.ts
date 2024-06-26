import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySubmissionComponent } from './my-submission.component';

describe('MySubmissionComponent', () => {
  let component: MySubmissionComponent;
  let fixture: ComponentFixture<MySubmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MySubmissionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MySubmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
