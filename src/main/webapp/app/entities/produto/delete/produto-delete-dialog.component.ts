import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProduto } from '../produto.model';
import { ProdutoService } from '../service/produto.service';

@Component({
  templateUrl: './produto-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProdutoDeleteDialogComponent {
  produto?: IProduto;

  protected produtoService = inject(ProdutoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.produtoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
