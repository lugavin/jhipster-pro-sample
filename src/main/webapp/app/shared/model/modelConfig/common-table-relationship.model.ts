import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import { IDataDictionary } from '@/shared/model/settings/data-dictionary.model';

import { RelationshipType } from '@/shared/model/enumerations/relationship-type.model';
import { SourceType } from '@/shared/model/enumerations/source-type.model';
import { FixedType } from '@/shared/model/enumerations/fixed-type.model';
import { EndUsedType } from '@/shared/model/enumerations/end-used-type.model';
export interface ICommonTableRelationship {
  id?: number;
  name?: string;
  relationshipType?: RelationshipType;
  sourceType?: SourceType;
  otherEntityField?: string | null;
  otherEntityName?: string;
  relationshipName?: string;
  otherEntityRelationshipName?: string | null;
  columnWidth?: number | null;
  order?: number | null;
  fixed?: FixedType | null;
  editInList?: boolean | null;
  enableFilter?: boolean | null;
  hideInList?: boolean | null;
  hideInForm?: boolean | null;
  system?: boolean | null;
  fontColor?: string | null;
  backgroundColor?: string | null;
  help?: string | null;
  ownerSide?: boolean | null;
  dataName?: string;
  webComponentType?: string | null;
  otherEntityIsTree?: boolean | null;
  showInFilterTree?: boolean | null;
  dataDictionaryCode?: string | null;
  clientReadOnly?: boolean | null;
  endUsed?: EndUsedType | null;
  options?: string | null;
  relationEntity?: ICommonTable | null;
  dataDictionaryNode?: IDataDictionary | null;
  metaModel?: ICommonTable | null;
  commonTable?: ICommonTable | null;
}

export class CommonTableRelationship implements ICommonTableRelationship {
  constructor(
    public id?: number,
    public name?: string,
    public relationshipType?: RelationshipType,
    public sourceType?: SourceType,
    public otherEntityField?: string | null,
    public otherEntityName?: string,
    public relationshipName?: string,
    public otherEntityRelationshipName?: string | null,
    public columnWidth?: number | null,
    public order?: number | null,
    public fixed?: FixedType | null,
    public editInList?: boolean | null,
    public enableFilter?: boolean | null,
    public hideInList?: boolean | null,
    public hideInForm?: boolean | null,
    public system?: boolean | null,
    public fontColor?: string | null,
    public backgroundColor?: string | null,
    public help?: string | null,
    public ownerSide?: boolean | null,
    public dataName?: string,
    public webComponentType?: string | null,
    public otherEntityIsTree?: boolean | null,
    public showInFilterTree?: boolean | null,
    public dataDictionaryCode?: string | null,
    public clientReadOnly?: boolean | null,
    public endUsed?: EndUsedType | null,
    public options?: string | null,
    public relationEntity?: ICommonTable | null,
    public dataDictionaryNode?: IDataDictionary | null,
    public metaModel?: ICommonTable | null,
    public commonTable?: ICommonTable | null
  ) {
    this.editInList = this.editInList ?? false;
    this.enableFilter = this.enableFilter ?? false;
    this.hideInList = this.hideInList ?? false;
    this.hideInForm = this.hideInForm ?? false;
    this.system = this.system ?? false;
    this.ownerSide = this.ownerSide ?? false;
    this.otherEntityIsTree = this.otherEntityIsTree ?? false;
    this.showInFilterTree = this.showInFilterTree ?? false;
    this.clientReadOnly = this.clientReadOnly ?? false;
  }
}
