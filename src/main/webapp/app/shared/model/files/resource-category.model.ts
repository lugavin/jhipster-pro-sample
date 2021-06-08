import { IUploadFile } from '@/shared/model/files/upload-file.model';
import { IUploadImage } from '@/shared/model/files/upload-image.model';

export interface IResourceCategory {
  id?: number;
  title?: string | null;
  code?: string | null;
  sort?: number | null;
  files?: IUploadFile[] | null;
  children?: IResourceCategory[] | null;
  images?: IUploadImage[] | null;
  parent?: IResourceCategory | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class ResourceCategory implements IResourceCategory {
  constructor(
    public id?: number,
    public title?: string | null,
    public code?: string | null,
    public sort?: number | null,
    public files?: IUploadFile[] | null,
    public children?: IResourceCategory[] | null,
    public images?: IUploadImage[] | null,
    public parent?: IResourceCategory | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}
