import { ICommonTableField } from '@/shared/model/modelConfig/common-table-field.model';
import { ICommonTableRelationship } from '@/shared/model/modelConfig/common-table-relationship.model';
import { IUser } from '@/shared/model/user.model';
import { IBusinessType } from '@/shared/model/settings/business-type.model';
import { Moment } from 'moment';

export interface ICommonTable {
  id?: number;
  name?: string;
  entityName?: string;
  tableName?: string;
  system?: boolean | null;
  clazzName?: string;
  generated?: boolean | null;
  creatAt?: Moment | null;
  generateAt?: Moment | null;
  generateClassAt?: Moment | null;
  description?: string | null;
  treeTable?: boolean | null;
  baseTableId?: number | null;
  recordActionWidth?: number | null;
  listConfig?: string | null;
  formConfig?: string | null;
  editInModal?: boolean | null;
  commonTableFields?: ICommonTableField[] | null;
  relationships?: ICommonTableRelationship[] | null;
  metaModel?: ICommonTable | null;
  creator?: IUser | null;
  businessType?: IBusinessType | null;
}

export class CommonTable implements ICommonTable {
  constructor(
    public id?: number,
    public name?: string,
    public entityName?: string,
    public tableName?: string,
    public system?: boolean | null,
    public clazzName?: string,
    public generated?: boolean | null,
    public creatAt?: Moment | null,
    public generateAt?: Moment | null,
    public generateClassAt?: Moment | null,
    public description?: string | null,
    public treeTable?: boolean | null,
    public baseTableId?: number | null,
    public recordActionWidth?: number | null,
    public listConfig?: string | null,
    public formConfig?: string | null,
    public editInModal?: boolean | null,
    public commonTableFields?: ICommonTableField[] | null,
    public relationships?: ICommonTableRelationship[] | null,
    public metaModel?: ICommonTable | null,
    public creator?: IUser | null,
    public businessType?: IBusinessType | null
  ) {
    this.system = this.system ?? false;
    this.generated = this.generated ?? false;
    this.treeTable = this.treeTable ?? false;
    this.editInModal = this.editInModal ?? false;
  }
}
