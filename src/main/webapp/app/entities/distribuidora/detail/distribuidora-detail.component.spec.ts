import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DistribuidoraDetailComponent } from './distribuidora-detail.component';

describe('Distribuidora Management Detail Component', () => {
  let comp: DistribuidoraDetailComponent;
  let fixture: ComponentFixture<DistribuidoraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DistribuidoraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./distribuidora-detail.component').then(m => m.DistribuidoraDetailComponent),
              resolve: { distribuidora: () => of({ id: 10857 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DistribuidoraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DistribuidoraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load distribuidora on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DistribuidoraDetailComponent);

      // THEN
      expect(instance.distribuidora()).toEqual(expect.objectContaining({ id: 10857 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
