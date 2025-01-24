import { ITipo } from 'app/entities/tipo/tipo.model';
import { IDistribuidora } from 'app/entities/distribuidora/distribuidora.model';

export interface IProduto {
  id: number;
  nome?: string | null;
  valor?: number | null;
  quantidade?: number | null;
  tipo?: ITipo | null;
  distribuidora?: IDistribuidora | null;
}

export type NewProduto = Omit<IProduto, 'id'> & { id: null };
