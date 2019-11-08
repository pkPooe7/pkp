import { IClassification } from 'app/shared/model/classification.model';
import { IWorld } from 'app/shared/model/world.model';

export interface IAlien {
  id?: number;
  name?: string;
  species?: string;
  planet?: string;
  catergory?: IClassification;
  homeWorld?: IWorld;
}

export class Alien implements IAlien {
  constructor(
    public id?: number,
    public name?: string,
    public species?: string,
    public planet?: string,
    public catergory?: IClassification,
    public homeWorld?: IWorld
  ) {}
}
