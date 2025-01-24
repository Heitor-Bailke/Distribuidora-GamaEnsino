import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipo } from 'app/entities/tipo/tipo.model';
import { TipoService } from 'app/entities/tipo/service/tipo.service';
import { IDistribuidora } from 'app/entities/distribuidora/distribuidora.model';
import { DistribuidoraService } from 'app/entities/distribuidora/service/distribuidora.service';
import { ProdutoService } from '../service/produto.service';
import { IProduto } from '../produto.model';
import { ProdutoFormGroup, ProdutoFormService } from './produto-form.service';

@Component({
  selector: 'jhi-produto-update',
  templateUrl: './produto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProdutoUpdateComponent implements OnInit {
  isSaving = false;
  produto: IProduto | null = null;

  tiposSharedCollection: ITipo[] = [];
  distribuidorasSharedCollection: IDistribuidora[] = [];

  protected produtoService = inject(ProdutoService);
  protected produtoFormService = inject(ProdutoFormService);
  protected tipoService = inject(TipoService);
  protected distribuidoraService = inject(DistribuidoraService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProdutoFormGroup = this.produtoFormService.createProdutoFormGroup();

  compareTipo = (o1: ITipo | null, o2: ITipo | null): boolean => this.tipoService.compareTipo(o1, o2);

  compareDistribuidora = (o1: IDistribuidora | null, o2: IDistribuidora | null): boolean =>
    this.distribuidoraService.compareDistribuidora(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produto }) => {
      this.produto = produto;
      if (produto) {
        this.updateForm(produto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produto = this.produtoFormService.getProduto(this.editForm);
    if (produto.id !== null) {
      this.subscribeToSaveResponse(this.produtoService.update(produto));
    } else {
      this.subscribeToSaveResponse(this.produtoService.create(produto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduto>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(produto: IProduto): void {
    this.produto = produto;
    this.produtoFormService.resetForm(this.editForm, produto);

    this.tiposSharedCollection = this.tipoService.addTipoToCollectionIfMissing<ITipo>(this.tiposSharedCollection, produto.tipo);
    this.distribuidorasSharedCollection = this.distribuidoraService.addDistribuidoraToCollectionIfMissing<IDistribuidora>(
      this.distribuidorasSharedCollection,
      produto.distribuidora,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoService
      .query()
      .pipe(map((res: HttpResponse<ITipo[]>) => res.body ?? []))
      .pipe(map((tipos: ITipo[]) => this.tipoService.addTipoToCollectionIfMissing<ITipo>(tipos, this.produto?.tipo)))
      .subscribe((tipos: ITipo[]) => (this.tiposSharedCollection = tipos));

    this.distribuidoraService
      .query()
      .pipe(map((res: HttpResponse<IDistribuidora[]>) => res.body ?? []))
      .pipe(
        map((distribuidoras: IDistribuidora[]) =>
          this.distribuidoraService.addDistribuidoraToCollectionIfMissing<IDistribuidora>(distribuidoras, this.produto?.distribuidora),
        ),
      )
      .subscribe((distribuidoras: IDistribuidora[]) => (this.distribuidorasSharedCollection = distribuidoras));
  }
}
