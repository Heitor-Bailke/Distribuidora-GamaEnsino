export interface ITipo {
  id: number;
  name?: string | null;
}

export type NewTipo = Omit<ITipo, 'id'> & { id: null };
