import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITipo } from 'app/entities/tipo/tipo.model';
import { TipoService } from 'app/entities/tipo/service/tipo.service';
import { IDistribuidora } from 'app/entities/distribuidora/distribuidora.model';
import { DistribuidoraService } from 'app/entities/distribuidora/service/distribuidora.service';
import { IProduto } from '../produto.model';
import { ProdutoService } from '../service/produto.service';
import { ProdutoFormService } from './produto-form.service';

import { ProdutoUpdateComponent } from './produto-update.component';

describe('Produto Management Update Component', () => {
  let comp: ProdutoUpdateComponent;
  let fixture: ComponentFixture<ProdutoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let produtoFormService: ProdutoFormService;
  let produtoService: ProdutoService;
  let tipoService: TipoService;
  let distribuidoraService: DistribuidoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProdutoUpdateComponent],
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
      .overrideTemplate(ProdutoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProdutoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    produtoFormService = TestBed.inject(ProdutoFormService);
    produtoService = TestBed.inject(ProdutoService);
    tipoService = TestBed.inject(TipoService);
    distribuidoraService = TestBed.inject(DistribuidoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tipo query and add missing value', () => {
      const produto: IProduto = { id: 14085 };
      const tipo: ITipo = { id: 28268 };
      produto.tipo = tipo;

      const tipoCollection: ITipo[] = [{ id: 28268 }];
      jest.spyOn(tipoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoCollection })));
      const additionalTipos = [tipo];
      const expectedCollection: ITipo[] = [...additionalTipos, ...tipoCollection];
      jest.spyOn(tipoService, 'addTipoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      expect(tipoService.query).toHaveBeenCalled();
      expect(tipoService.addTipoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoCollection,
        ...additionalTipos.map(expect.objectContaining),
      );
      expect(comp.tiposSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Distribuidora query and add missing value', () => {
      const produto: IProduto = { id: 14085 };
      const distribuidora: IDistribuidora = { id: 10857 };
      produto.distribuidora = distribuidora;

      const distribuidoraCollection: IDistribuidora[] = [{ id: 10857 }];
      jest.spyOn(distribuidoraService, 'query').mockReturnValue(of(new HttpResponse({ body: distribuidoraCollection })));
      const additionalDistribuidoras = [distribuidora];
      const expectedCollection: IDistribuidora[] = [...additionalDistribuidoras, ...distribuidoraCollection];
      jest.spyOn(distribuidoraService, 'addDistribuidoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      expect(distribuidoraService.query).toHaveBeenCalled();
      expect(distribuidoraService.addDistribuidoraToCollectionIfMissing).toHaveBeenCalledWith(
        distribuidoraCollection,
        ...additionalDistribuidoras.map(expect.objectContaining),
      );
      expect(comp.distribuidorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const produto: IProduto = { id: 14085 };
      const tipo: ITipo = { id: 28268 };
      produto.tipo = tipo;
      const distribuidora: IDistribuidora = { id: 10857 };
      produto.distribuidora = distribuidora;

      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      expect(comp.tiposSharedCollection).toContainEqual(tipo);
      expect(comp.distribuidorasSharedCollection).toContainEqual(distribuidora);
      expect(comp.produto).toEqual(produto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduto>>();
      const produto = { id: 3606 };
      jest.spyOn(produtoFormService, 'getProduto').mockReturnValue(produto);
      jest.spyOn(produtoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produto }));
      saveSubject.complete();

      // THEN
      expect(produtoFormService.getProduto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(produtoService.update).toHaveBeenCalledWith(expect.objectContaining(produto));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduto>>();
      const produto = { id: 3606 };
      jest.spyOn(produtoFormService, 'getProduto').mockReturnValue({ id: null });
      jest.spyOn(produtoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produto }));
      saveSubject.complete();

      // THEN
      expect(produtoFormService.getProduto).toHaveBeenCalled();
      expect(produtoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduto>>();
      const produto = { id: 3606 };
      jest.spyOn(produtoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(produtoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTipo', () => {
      it('Should forward to tipoService', () => {
        const entity = { id: 28268 };
        const entity2 = { id: 8133 };
        jest.spyOn(tipoService, 'compareTipo');
        comp.compareTipo(entity, entity2);
        expect(tipoService.compareTipo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDistribuidora', () => {
      it('Should forward to distribuidoraService', () => {
        const entity = { id: 10857 };
        const entity2 = { id: 22843 };
        jest.spyOn(distribuidoraService, 'compareDistribuidora');
        comp.compareDistribuidora(entity, entity2);
        expect(distribuidoraService.compareDistribuidora).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
