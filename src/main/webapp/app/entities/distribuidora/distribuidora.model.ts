export interface IDistribuidora {
  id: number;
  nome?: string | null;
  cep?: string | null;
  cidade?: string | null;
  bairro?: string | null;
  rua?: string | null;
  numero?: number | null;
  referencia?: string | null;
  estado?: string | null;
  detalhes?: string | null;
}

export type NewDistribuidora = Omit<IDistribuidora, 'id'> & { id: null };
