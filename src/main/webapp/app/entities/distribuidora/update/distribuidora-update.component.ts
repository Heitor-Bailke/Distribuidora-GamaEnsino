import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDistribuidora } from '../distribuidora.model';
import { DistribuidoraService } from '../service/distribuidora.service';
import { DistribuidoraFormGroup, DistribuidoraFormService } from './distribuidora-form.service';

@Component({
  selector: 'jhi-distribuidora-update',
  templateUrl: './distribuidora-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DistribuidoraUpdateComponent implements OnInit {
  isSaving = false;
  distribuidora: IDistribuidora | null = null;

  protected distribuidoraService = inject(DistribuidoraService);
  protected distribuidoraFormService = inject(DistribuidoraFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DistribuidoraFormGroup = this.distribuidoraFormService.createDistribuidoraFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ distribuidora }) => {
      this.distribuidora = distribuidora;
      if (distribuidora) {
        this.updateForm(distribuidora);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const distribuidora = this.distribuidoraFormService.getDistribuidora(this.editForm);
    if (distribuidora.id !== null) {
      this.subscribeToSaveResponse(this.distribuidoraService.update(distribuidora));
    } else {
      this.subscribeToSaveResponse(this.distribuidoraService.create(distribuidora));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistribuidora>>): void {
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

  protected updateForm(distribuidora: IDistribuidora): void {
    this.distribuidora = distribuidora;
    this.distribuidoraFormService.resetForm(this.editForm, distribuidora);
  }
}
