import { IWorld } from 'app/shared/model/world.model';

export interface ISolarSystem {
  id?: number;
  name?: string;
  galaxy?: string;
  worldOrigins?: IWorld[];
}

export class SolarSystem implements ISolarSystem {
  constructor(public id?: number, public name?: string, public galaxy?: string, public worldOrigins?: IWorld[]) {}
}
