import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import { Moment } from 'moment';

export interface IUReportFile {
  id?: number;
  name?: string | null;
  content?: string | null;
  createAt?: Moment | null;
  updateAt?: Moment | null;
  commonTable?: ICommonTable | null;
}

export class UReportFile implements IUReportFile {
  constructor(
    public id?: number,
    public name?: string | null,
    public content?: string | null,
    public createAt?: Moment | null,
    public updateAt?: Moment | null,
    public commonTable?: ICommonTable | null
  ) {}
}
