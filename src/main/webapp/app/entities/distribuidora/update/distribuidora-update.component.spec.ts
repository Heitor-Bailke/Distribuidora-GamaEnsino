import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DistribuidoraService } from '../service/distribuidora.service';
import { IDistribuidora } from '../distribuidora.model';
import { DistribuidoraFormService } from './distribuidora-form.service';

import { DistribuidoraUpdateComponent } from './distribuidora-update.component';

describe('Distribuidora Management Update Component', () => {
  let comp: DistribuidoraUpdateComponent;
  let fixture: ComponentFixture<DistribuidoraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let distribuidoraFormService: DistribuidoraFormService;
  let distribuidoraService: DistribuidoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DistribuidoraUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DistribuidoraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DistribuidoraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    distribuidoraFormService = TestBed.inject(DistribuidoraFormService);
    distribuidoraService = TestBed.inject(DistribuidoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const distribuidora: IDistribuidora = { id: 22843 };

      activatedRoute.data = of({ distribuidora });
      comp.ngOnInit();

      expect(comp.distribuidora).toEqual(distribuidora);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistribuidora>>();
      const distribuidora = { id: 10857 };
      jest.spyOn(distribuidoraFormService, 'getDistribuidora').mockReturnValue(distribuidora);
      jest.spyOn(distribuidoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ distribuidora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: distribuidora }));
      saveSubject.complete();

      // THEN
      expect(distribuidoraFormService.getDistribuidora).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(distribuidoraService.update).toHaveBeenCalledWith(expect.objectContaining(distribuidora));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistribuidora>>();
      const distribuidora = { id: 10857 };
      jest.spyOn(distribuidoraFormService, 'getDistribuidora').mockReturnValue({ id: null });
      jest.spyOn(distribuidoraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ distribuidora: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: distribuidora }));
      saveSubject.complete();

      // THEN
      expect(distribuidoraFormService.getDistribuidora).toHaveBeenCalled();
      expect(distribuidoraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistribuidora>>();
      const distribuidora = { id: 10857 };
      jest.spyOn(distribuidoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ distribuidora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(distribuidoraService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
