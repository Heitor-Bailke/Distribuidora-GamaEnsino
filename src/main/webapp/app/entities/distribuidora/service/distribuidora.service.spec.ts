import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDistribuidora } from '../distribuidora.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../distribuidora.test-samples';

import { DistribuidoraService } from './distribuidora.service';

const requireRestSample: IDistribuidora = {
  ...sampleWithRequiredData,
};

describe('Distribuidora Service', () => {
  let service: DistribuidoraService;
  let httpMock: HttpTestingController;
  let expectedResult: IDistribuidora | IDistribuidora[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DistribuidoraService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Distribuidora', () => {
      const distribuidora = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(distribuidora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Distribuidora', () => {
      const distribuidora = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(distribuidora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Distribuidora', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Distribuidora', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Distribuidora', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDistribuidoraToCollectionIfMissing', () => {
      it('should add a Distribuidora to an empty array', () => {
        const distribuidora: IDistribuidora = sampleWithRequiredData;
        expectedResult = service.addDistribuidoraToCollectionIfMissing([], distribuidora);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(distribuidora);
      });

      it('should not add a Distribuidora to an array that contains it', () => {
        const distribuidora: IDistribuidora = sampleWithRequiredData;
        const distribuidoraCollection: IDistribuidora[] = [
          {
            ...distribuidora,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDistribuidoraToCollectionIfMissing(distribuidoraCollection, distribuidora);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Distribuidora to an array that doesn't contain it", () => {
        const distribuidora: IDistribuidora = sampleWithRequiredData;
        const distribuidoraCollection: IDistribuidora[] = [sampleWithPartialData];
        expectedResult = service.addDistribuidoraToCollectionIfMissing(distribuidoraCollection, distribuidora);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(distribuidora);
      });

      it('should add only unique Distribuidora to an array', () => {
        const distribuidoraArray: IDistribuidora[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const distribuidoraCollection: IDistribuidora[] = [sampleWithRequiredData];
        expectedResult = service.addDistribuidoraToCollectionIfMissing(distribuidoraCollection, ...distribuidoraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const distribuidora: IDistribuidora = sampleWithRequiredData;
        const distribuidora2: IDistribuidora = sampleWithPartialData;
        expectedResult = service.addDistribuidoraToCollectionIfMissing([], distribuidora, distribuidora2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(distribuidora);
        expect(expectedResult).toContain(distribuidora2);
      });

      it('should accept null and undefined values', () => {
        const distribuidora: IDistribuidora = sampleWithRequiredData;
        expectedResult = service.addDistribuidoraToCollectionIfMissing([], null, distribuidora, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(distribuidora);
      });

      it('should return initial array if no Distribuidora is added', () => {
        const distribuidoraCollection: IDistribuidora[] = [sampleWithRequiredData];
        expectedResult = service.addDistribuidoraToCollectionIfMissing(distribuidoraCollection, undefined, null);
        expect(expectedResult).toEqual(distribuidoraCollection);
      });
    });

    describe('compareDistribuidora', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDistribuidora(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 10857 };
        const entity2 = null;

        const compareResult1 = service.compareDistribuidora(entity1, entity2);
        const compareResult2 = service.compareDistribuidora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 10857 };
        const entity2 = { id: 22843 };

        const compareResult1 = service.compareDistribuidora(entity1, entity2);
        const compareResult2 = service.compareDistribuidora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 10857 };
        const entity2 = { id: 10857 };

        const compareResult1 = service.compareDistribuidora(entity1, entity2);
        const compareResult2 = service.compareDistribuidora(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
