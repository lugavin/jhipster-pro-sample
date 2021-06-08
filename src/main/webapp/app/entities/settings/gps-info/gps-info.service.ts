import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IGpsInfo } from '@/shared/model/settings/gps-info.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/gps-infos';
type EntityResponseType = AxiosResponse<IGpsInfo>;
type EntityArrayResponseType = AxiosResponse<IGpsInfo[]>;

export default class GpsInfoService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IGpsInfo>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IGpsInfo[]>(baseApiUrl, {
      params: options,
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    });
  }

  public delete(id: number): Observable<AxiosResponse> {
    return Axios.delete(`${baseApiUrl}/${id}`);
  }

  public deleteByIds(ids: string[]): Observable<AxiosResponse> {
    return Axios.delete(`${baseApiUrl}`, {
      params: { ids },
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    });
  }

  public create(entity: IGpsInfo): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: IGpsInfo): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(gpsInfo: IGpsInfo, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { gpsInfo, specifiedFields });
  }

  updateBySpecifiedField(gpsInfo: IGpsInfo, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { gpsInfo, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
