import { GpsType } from '@/shared/model/enumerations/gps-type.model';
export interface IGpsInfo {
  id?: number;
  type?: GpsType | null;
  latitude?: number | null;
  longitude?: number | null;
  address?: string | null;
}

export class GpsInfo implements IGpsInfo {
  constructor(
    public id?: number,
    public type?: GpsType | null,
    public latitude?: number | null,
    public longitude?: number | null,
    public address?: string | null
  ) {}
}
