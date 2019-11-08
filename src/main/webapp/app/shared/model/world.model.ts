import { IAlien } from 'app/shared/model/alien.model';
import { ISolarSystem } from 'app/shared/model/solar-system.model';

export interface IWorld {
  id?: number;
  name?: string;
  system?: string;
  alienWorlds?: IAlien[];
  homeSystem?: ISolarSystem;
}

export class World implements IWorld {
  constructor(
    public id?: number,
    public name?: string,
    public system?: string,
    public alienWorlds?: IAlien[],
    public homeSystem?: ISolarSystem
  ) {}
}
