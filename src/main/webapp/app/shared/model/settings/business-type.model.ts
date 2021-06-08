export interface IBusinessType {
  id?: number;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  icon?: string | null;
}

export class BusinessType implements IBusinessType {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public icon?: string | null
  ) {}
}
