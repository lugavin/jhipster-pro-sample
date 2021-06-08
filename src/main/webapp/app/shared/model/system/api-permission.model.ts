import { IAuthority } from '@/shared/model/system/authority.model';

import { ApiPermissionType } from '@/shared/model/enumerations/api-permission-type.model';
export interface IApiPermission {
  id?: number;
  serviceName?: string | null;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  type?: ApiPermissionType | null;
  method?: string | null;
  url?: string | null;
  children?: IApiPermission[] | null;
  parent?: IApiPermission | null;
  authorities?: IAuthority[] | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class ApiPermission implements IApiPermission {
  constructor(
    public id?: number,
    public serviceName?: string | null,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public type?: ApiPermissionType | null,
    public method?: string | null,
    public url?: string | null,
    public children?: IApiPermission[] | null,
    public parent?: IApiPermission | null,
    public authorities?: IAuthority[] | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}
