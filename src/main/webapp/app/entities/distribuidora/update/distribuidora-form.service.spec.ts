import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../distribuidora.test-samples';

import { DistribuidoraFormService } from './distribuidora-form.service';

describe('Distribuidora Form Service', () => {
  let service: DistribuidoraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DistribuidoraFormService);
  });

  describe('Service methods', () => {
    describe('createDistribuidoraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDistribuidoraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cnpj: expect.any(Object),
            contato: expect.any(Object),
            cep: expect.any(Object),
            cidade: expect.any(Object),
            bairro: expect.any(Object),
            rua: expect.any(Object),
            numero: expect.any(Object),
            referencia: expect.any(Object),
            estado: expect.any(Object),
            detalhes: expect.any(Object),
          }),
        );
      });

      it('passing IDistribuidora should create a new form with FormGroup', () => {
        const formGroup = service.createDistribuidoraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cnpj: expect.any(Object),
            contato: expect.any(Object),
            cep: expect.any(Object),
            cidade: expect.any(Object),
            bairro: expect.any(Object),
            rua: expect.any(Object),
            numero: expect.any(Object),
            referencia: expect.any(Object),
            estado: expect.any(Object),
            detalhes: expect.any(Object),
          }),
        );
      });
    });

    describe('getDistribuidora', () => {
      it('should return NewDistribuidora for default Distribuidora initial value', () => {
        const formGroup = service.createDistribuidoraFormGroup(sampleWithNewData);

        const distribuidora = service.getDistribuidora(formGroup) as any;

        expect(distribuidora).toMatchObject(sampleWithNewData);
      });

      it('should return NewDistribuidora for empty Distribuidora initial value', () => {
        const formGroup = service.createDistribuidoraFormGroup();

        const distribuidora = service.getDistribuidora(formGroup) as any;

        expect(distribuidora).toMatchObject({});
      });

      it('should return IDistribuidora', () => {
        const formGroup = service.createDistribuidoraFormGroup(sampleWithRequiredData);

        const distribuidora = service.getDistribuidora(formGroup) as any;

        expect(distribuidora).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDistribuidora should not enable id FormControl', () => {
        const formGroup = service.createDistribuidoraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDistribuidora should disable id FormControl', () => {
        const formGroup = service.createDistribuidoraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
