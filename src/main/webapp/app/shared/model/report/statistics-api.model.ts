import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import { IUser } from '@/shared/model/user.model';
import { Moment } from 'moment';

import { StatSourceType } from '@/shared/model/enumerations/stat-source-type.model';
export interface IStatisticsApi {
  id?: number;
  title?: string | null;
  apiKey?: string | null;
  createAt?: Moment | null;
  updateAt?: Moment | null;
  sourceType?: StatSourceType | null;
  apiBody?: string | null;
  result?: string | null;
  updateInterval?: number | null;
  lastSQLRunTime?: Moment | null;
  enable?: boolean | null;
  commonTable?: ICommonTable | null;
  creator?: IUser | null;
  modifier?: IUser | null;
}

export class StatisticsApi implements IStatisticsApi {
  constructor(
    public id?: number,
    public title?: string | null,
    public apiKey?: string | null,
    public createAt?: Moment | null,
    public updateAt?: Moment | null,
    public sourceType?: StatSourceType | null,
    public apiBody?: string | null,
    public result?: string | null,
    public updateInterval?: number | null,
    public lastSQLRunTime?: Moment | null,
    public enable?: boolean | null,
    public commonTable?: ICommonTable | null,
    public creator?: IUser | null,
    public modifier?: IUser | null
  ) {
    this.enable = this.enable ?? false;
  }
}
