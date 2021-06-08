import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';

import { CommonFieldType } from '@/shared/model/enumerations/common-field-type.model';
import { FixedType } from '@/shared/model/enumerations/fixed-type.model';
import { EndUsedType } from '@/shared/model/enumerations/end-used-type.model';
export interface ICommonTableField {
  id?: number;
  title?: string;
  entityFieldName?: string;
  type?: CommonFieldType;
  tableColumnName?: string;
  columnWidth?: number | null;
  order?: number | null;
  editInList?: boolean | null;
  hideInList?: boolean | null;
  hideInForm?: boolean | null;
  enableFilter?: boolean | null;
  validateRules?: string | null;
  showInFilterTree?: boolean | null;
  fixed?: FixedType | null;
  sortable?: boolean | null;
  treeIndicator?: boolean | null;
  clientReadOnly?: boolean | null;
  fieldValues?: string | null;
  notNull?: boolean | null;
  system?: boolean | null;
  help?: string | null;
  fontColor?: string | null;
  backgroundColor?: string | null;
  nullHideInForm?: boolean | null;
  endUsed?: EndUsedType | null;
  options?: string | null;
  metaModel?: ICommonTable | null;
  commonTable?: ICommonTable | null;
}

export class CommonTableField implements ICommonTableField {
  constructor(
    public id?: number,
    public title?: string,
    public entityFieldName?: string,
    public type?: CommonFieldType,
    public tableColumnName?: string,
    public columnWidth?: number | null,
    public order?: number | null,
    public editInList?: boolean | null,
    public hideInList?: boolean | null,
    public hideInForm?: boolean | null,
    public enableFilter?: boolean | null,
    public validateRules?: string | null,
    public showInFilterTree?: boolean | null,
    public fixed?: FixedType | null,
    public sortable?: boolean | null,
    public treeIndicator?: boolean | null,
    public clientReadOnly?: boolean | null,
    public fieldValues?: string | null,
    public notNull?: boolean | null,
    public system?: boolean | null,
    public help?: string | null,
    public fontColor?: string | null,
    public backgroundColor?: string | null,
    public nullHideInForm?: boolean | null,
    public endUsed?: EndUsedType | null,
    public options?: string | null,
    public metaModel?: ICommonTable | null,
    public commonTable?: ICommonTable | null
  ) {
    this.editInList = this.editInList ?? false;
    this.hideInList = this.hideInList ?? false;
    this.hideInForm = this.hideInForm ?? false;
    this.enableFilter = this.enableFilter ?? false;
    this.showInFilterTree = this.showInFilterTree ?? false;
    this.sortable = this.sortable ?? false;
    this.treeIndicator = this.treeIndicator ?? false;
    this.clientReadOnly = this.clientReadOnly ?? false;
    this.notNull = this.notNull ?? false;
    this.system = this.system ?? false;
    this.nullHideInForm = this.nullHideInForm ?? false;
  }
}
