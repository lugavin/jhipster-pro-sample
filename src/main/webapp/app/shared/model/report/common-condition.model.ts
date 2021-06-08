import { ICommonConditionItem } from '@/shared/model/report/common-condition-item.model';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import { Moment } from 'moment';

export interface ICommonCondition {
  id?: number;
  name?: string;
  description?: string | null;
  lastModifiedTime?: Moment | null;
  items?: ICommonConditionItem[] | null;
  commonTable?: ICommonTable | null;
}

export class CommonCondition implements ICommonCondition {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public lastModifiedTime?: Moment | null,
    public items?: ICommonConditionItem[] | null,
    public commonTable?: ICommonTable | null
  ) {}
}
