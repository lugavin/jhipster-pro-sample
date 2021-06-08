export interface IAdministrativeDivision {
  id?: number;
  name?: string | null;
  areaCode?: string | null;
  cityCode?: string | null;
  mergerName?: string | null;
  shortName?: string | null;
  zipCode?: string | null;
  level?: number | null;
  lng?: number | null;
  lat?: number | null;
  children?: IAdministrativeDivision[] | null;
  parent?: IAdministrativeDivision | null;
  expand?: boolean;
  nzAddLevel?: number;
}

export class AdministrativeDivision implements IAdministrativeDivision {
  constructor(
    public id?: number,
    public name?: string | null,
    public areaCode?: string | null,
    public cityCode?: string | null,
    public mergerName?: string | null,
    public shortName?: string | null,
    public zipCode?: string | null,
    public level?: number | null,
    public lng?: number | null,
    public lat?: number | null,
    public children?: IAdministrativeDivision[] | null,
    public parent?: IAdministrativeDivision | null,
    public expand?: boolean,
    public nzAddLevel?: number
  ) {}
}
