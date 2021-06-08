export interface IDataDictionary {
  id?: number;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  fontColor?: string | null;
  backgroundColor?: string | null;
  children?: IDataDictionary[] | null;
  parent?: IDataDictionary | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class DataDictionary implements IDataDictionary {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public fontColor?: string | null,
    public backgroundColor?: string | null,
    public children?: IDataDictionary[] | null,
    public parent?: IDataDictionary | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}
