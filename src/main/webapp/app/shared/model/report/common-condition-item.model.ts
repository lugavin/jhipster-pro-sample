import { ICommonCondition } from '@/shared/model/report/common-condition.model';

export interface ICommonConditionItem {
  id?: number;
  prefix?: string | null;
  fieldName?: string | null;
  fieldType?: string | null;
  operator?: string | null;
  value?: string | null;
  suffix?: string | null;
  order?: number | null;
  commonCondition?: ICommonCondition | null;
}

export class CommonConditionItem implements ICommonConditionItem {
  constructor(
    public id?: number,
    public prefix?: string | null,
    public fieldName?: string | null,
    public fieldType?: string | null,
    public operator?: string | null,
    public value?: string | null,
    public suffix?: string | null,
    public order?: number | null,
    public commonCondition?: ICommonCondition | null
  ) {}
}
