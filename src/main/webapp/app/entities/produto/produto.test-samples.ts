import { IProduto, NewProduto } from './produto.model';

export const sampleWithRequiredData: IProduto = {
  id: 30780,
  nome: 'realistic provided',
  valor: 7076.12,
  quantidade: 11400,
};

export const sampleWithPartialData: IProduto = {
  id: 7674,
  nome: 'where how fowl',
  valor: 18931.28,
  quantidade: 21277,
};

export const sampleWithFullData: IProduto = {
  id: 21138,
  nome: 'speedily toward between',
  valor: 24748.77,
  quantidade: 3310,
};

export const sampleWithNewData: NewProduto = {
  nome: 'impact',
  valor: 6127.74,
  quantidade: 20293,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
