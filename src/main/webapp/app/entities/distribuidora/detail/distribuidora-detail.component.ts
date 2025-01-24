import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDistribuidora } from '../distribuidora.model';

@Component({
  selector: 'jhi-distribuidora-detail',
  templateUrl: './distribuidora-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DistribuidoraDetailComponent {
  distribuidora = input<IDistribuidora | null>(null);

  previousState(): void {
    window.history.back();
  }
}
