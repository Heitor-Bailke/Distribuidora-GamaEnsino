import { IDistribuidora, NewDistribuidora } from './distribuidora.model';

export const sampleWithRequiredData: IDistribuidora = {
  id: 4060,
  nome: 'beyond toward',
  cep: 'tough wh',
  cidade: 'cleverly',
  bairro: 'doing',
  rua: 'retool festival however',
  numero: 3441,
  estado: 'um',
};

export const sampleWithPartialData: IDistribuidora = {
  id: 10005,
  nome: 'ack',
  cep: 'vividXXX',
  cidade: 'bitter sniveling symbolise',
  bairro: 'certification essential',
  rua: 'woot',
  numero: 12060,
  estado: 'sh',
  detalhes: 'calmly tenant soon',
};

export const sampleWithFullData: IDistribuidora = {
  id: 29819,
  nome: 'seemingly hmph',
  cep: 'sermon w',
  cidade: 'overproduce',
  bairro: 'cute',
  rua: 'drain boo',
  numero: 27313,
  referencia: 'bind although',
  estado: 'ri',
  detalhes: 'overload freely',
};

export const sampleWithNewData: NewDistribuidora = {
  nome: 'whoa',
  cep: 'unfortun',
  cidade: 'outstanding',
  bairro: 'ack',
  rua: 'fooey',
  numero: 30816,
  estado: 'bl',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
