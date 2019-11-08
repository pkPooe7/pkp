import { IClassification } from 'app/shared/model/classification.model';

export interface ITechnology {
  id?: number;
  name?: string;
  aquired?: boolean;
  speci?: string;
  speciesTeches?: IClassification[];
}

export class Technology implements ITechnology {
  constructor(
    public id?: number,
    public name?: string,
    public aquired?: boolean,
    public speci?: string,
    public speciesTeches?: IClassification[]
  ) {
    this.aquired = this.aquired || false;
  }
}
