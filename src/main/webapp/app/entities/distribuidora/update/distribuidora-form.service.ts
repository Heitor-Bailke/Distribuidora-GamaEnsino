import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDistribuidora, NewDistribuidora } from '../distribuidora.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDistribuidora for edit and NewDistribuidoraFormGroupInput for create.
 */
type DistribuidoraFormGroupInput = IDistribuidora | PartialWithRequiredKeyOf<NewDistribuidora>;

type DistribuidoraFormDefaults = Pick<NewDistribuidora, 'id'>;

type DistribuidoraFormGroupContent = {
  id: FormControl<IDistribuidora['id'] | NewDistribuidora['id']>;
  nome: FormControl<IDistribuidora['nome']>;
  cnpj: FormControl<IDistribuidora['cnpj']>;
  contato: FormControl<IDistribuidora['contato']>;
  cep: FormControl<IDistribuidora['cep']>;
  cidade: FormControl<IDistribuidora['cidade']>;
  bairro: FormControl<IDistribuidora['bairro']>;
  rua: FormControl<IDistribuidora['rua']>;
  numero: FormControl<IDistribuidora['numero']>;
  referencia: FormControl<IDistribuidora['referencia']>;
  estado: FormControl<IDistribuidora['estado']>;
  detalhes: FormControl<IDistribuidora['detalhes']>;
};

export type DistribuidoraFormGroup = FormGroup<DistribuidoraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DistribuidoraFormService {
  createDistribuidoraFormGroup(distribuidora: DistribuidoraFormGroupInput = { id: null }): DistribuidoraFormGroup {
    const distribuidoraRawValue = {
      ...this.getFormDefaults(),
      ...distribuidora,
    };
    return new FormGroup<DistribuidoraFormGroupContent>({
      id: new FormControl(
        { value: distribuidoraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(distribuidoraRawValue.nome, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      cnpj: new FormControl(distribuidoraRawValue.cnpj, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      contato: new FormControl(distribuidoraRawValue.contato, {
        validators: [Validators.required],
      }),
      cep: new FormControl(distribuidoraRawValue.cep, {
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(8)],
      }),
      cidade: new FormControl(distribuidoraRawValue.cidade, {
        validators: [Validators.required],
      }),
      bairro: new FormControl(distribuidoraRawValue.bairro, {
        validators: [Validators.required],
      }),
      rua: new FormControl(distribuidoraRawValue.rua, {
        validators: [Validators.required],
      }),
      numero: new FormControl(distribuidoraRawValue.numero, {
        validators: [Validators.required],
      }),
      referencia: new FormControl(distribuidoraRawValue.referencia),
      estado: new FormControl(distribuidoraRawValue.estado, {
        validators: [Validators.required, Validators.maxLength(2)],
      }),
      detalhes: new FormControl(distribuidoraRawValue.detalhes, {
        validators: [Validators.minLength(3)],
      }),
    });
  }

  getDistribuidora(form: DistribuidoraFormGroup): IDistribuidora | NewDistribuidora {
    return form.getRawValue() as IDistribuidora | NewDistribuidora;
  }

  resetForm(form: DistribuidoraFormGroup, distribuidora: DistribuidoraFormGroupInput): void {
    const distribuidoraRawValue = { ...this.getFormDefaults(), ...distribuidora };
    form.reset(
      {
        ...distribuidoraRawValue,
        id: { value: distribuidoraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DistribuidoraFormDefaults {
    return {
      id: null,
    };
  }
}
