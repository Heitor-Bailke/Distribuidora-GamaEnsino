import { ITipo, NewTipo } from './tipo.model';

export const sampleWithRequiredData: ITipo = {
  id: 18676,
  name: 'entice',
};

export const sampleWithPartialData: ITipo = {
  id: 20225,
  name: 'round narrate ouch',
};

export const sampleWithFullData: ITipo = {
  id: 32611,
  name: 'militate monthly but',
};

export const sampleWithNewData: NewTipo = {
  name: 'individual yahoo parade',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
