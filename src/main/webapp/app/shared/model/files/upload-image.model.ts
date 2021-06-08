import { IUser } from '@/shared/model/user.model';
import { IResourceCategory } from '@/shared/model/files/resource-category.model';
import { Moment } from 'moment';

export interface IUploadImage {
  id?: number;
  fullName?: string | null;
  name?: string | null;
  ext?: string | null;
  type?: string | null;
  url?: string | null;
  path?: string | null;
  folder?: string | null;
  entityName?: string | null;
  createAt?: Moment | null;
  fileSize?: number | null;
  smartUrl?: string | null;
  mediumUrl?: string | null;
  referenceCount?: number | null;
  user?: IUser | null;
  category?: IResourceCategory | null;
}

export class UploadImage implements IUploadImage {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public name?: string | null,
    public ext?: string | null,
    public type?: string | null,
    public url?: string | null,
    public path?: string | null,
    public folder?: string | null,
    public entityName?: string | null,
    public createAt?: Moment | null,
    public fileSize?: number | null,
    public smartUrl?: string | null,
    public mediumUrl?: string | null,
    public referenceCount?: number | null,
    public user?: IUser | null,
    public category?: IResourceCategory | null
  ) {}
}
