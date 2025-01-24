import { IDistribuidora, NewDistribuidora } from './distribuidora.model';

export const sampleWithRequiredData: IDistribuidora = {
  id: 4060,
  nome: 'beyond toward',
  cnpj: 'tough whereas scratc',
  contato: 6259,
  cep: 'if doing',
  cidade: 'retool festival however',
  bairro: 'deduce',
  rua: 'sin restfully aha',
  numero: 22027,
  estado: 'ag',
};

export const sampleWithPartialData: IDistribuidora = {
  id: 10005,
  nome: 'ack',
  cnpj: 'vivid',
  contato: 26936,
  cep: 'triumpha',
  cidade: 'since',
  bairro: 'backbone',
  rua: 'the',
  numero: 30042,
  estado: 'im',
  detalhes: 'behind',
};

export const sampleWithFullData: IDistribuidora = {
  id: 29819,
  nome: 'seemingly hmph',
  cnpj: 'sermon whoa',
  contato: 3095,
  cep: 'booXXXXX',
  cidade: 'gee drain boo',
  bairro: 'spellcheck croon scoop',
  rua: 'geez',
  numero: 16384,
  referencia: 'overload freely',
  estado: 'de',
  detalhes: 'simple potentially',
};

export const sampleWithNewData: NewDistribuidora = {
  nome: 'whoa',
  cnpj: 'unfortunately',
  contato: 4238,
  cep: 'better a',
  cidade: 'fooey',
  bairro: 'wring holster aggressive',
  rua: 'before',
  numero: 10543,
  estado: 'bo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
