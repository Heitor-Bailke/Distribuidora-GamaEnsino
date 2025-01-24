import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipo } from '../tipo.model';

@Component({
  selector: 'jhi-tipo-detail',
  templateUrl: './tipo-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoDetailComponent {
  tipo = input<ITipo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
