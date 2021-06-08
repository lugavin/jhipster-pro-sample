import { IUser } from '@/shared/model/user.model';

export interface IPosition {
  id?: number;
  code?: string;
  name?: string;
  sortNo?: number | null;
  description?: string | null;
  users?: IUser[] | null;
}

export class Position implements IPosition {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public sortNo?: number | null,
    public description?: string | null,
    public users?: IUser[] | null
  ) {}
}
