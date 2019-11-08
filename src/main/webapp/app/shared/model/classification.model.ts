import { IAlien } from 'app/shared/model/alien.model';
import { ITechnology } from 'app/shared/model/technology.model';
import { Hands } from 'app/shared/model/enumerations/hands.model';

export interface IClassification {
  id?: number;
  name?: string;
  handed?: Hands;
  type?: IAlien;
  raceNames?: ITechnology[];
}

export class Classification implements IClassification {
  constructor(public id?: number, public name?: string, public handed?: Hands, public type?: IAlien, public raceNames?: ITechnology[]) {}
}
