import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDistribuidora } from '../distribuidora.model';
import { DistribuidoraService } from '../service/distribuidora.service';

@Component({
  templateUrl: './distribuidora-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DistribuidoraDeleteDialogComponent {
  distribuidora?: IDistribuidora;

  protected distribuidoraService = inject(DistribuidoraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.distribuidoraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
