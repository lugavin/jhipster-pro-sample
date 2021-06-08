import { IAuthority } from '@/shared/model/system/authority.model';
import { IUser } from '@/shared/model/user.model';
import { Moment } from 'moment';

export interface IDepartment {
  id?: number;
  name?: string | null;
  code?: string | null;
  address?: string | null;
  phoneNum?: string | null;
  logo?: string | null;
  contact?: string | null;
  createUserId?: number | null;
  createTime?: Moment | null;
  children?: IDepartment[] | null;
  authorities?: IAuthority[] | null;
  parent?: IDepartment | null;
  users?: IUser[] | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public address?: string | null,
    public phoneNum?: string | null,
    public logo?: string | null,
    public contact?: string | null,
    public createUserId?: number | null,
    public createTime?: Moment | null,
    public children?: IDepartment[] | null,
    public authorities?: IAuthority[] | null,
    public parent?: IDepartment | null,
    public users?: IUser[] | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}
