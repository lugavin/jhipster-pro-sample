import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IAdministrativeDivision } from '@/shared/model/settings/administrative-division.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/administrative-divisions';
type EntityResponseType = AxiosResponse<IAdministrativeDivision>;
type EntityArrayResponseType = AxiosResponse<IAdministrativeDivision[]>;

export default class AdministrativeDivisionService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IAdministrativeDivision>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IAdministrativeDivision[]>(baseApiUrl, {
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

  tree(): Observable<EntityArrayResponseType> {
    return Axios.get(`${baseApiUrl}/tree`);
  }

  treeByParentId(parentId: number): Observable<EntityArrayResponseType> {
    return Axios.get(`${baseApiUrl}/${parentId}/tree`);
  }

  public create(entity: IAdministrativeDivision): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: IAdministrativeDivision): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(administrativeDivision: IAdministrativeDivision, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { administrativeDivision, specifiedFields });
  }

  updateBySpecifiedField(
    administrativeDivision: IAdministrativeDivision,
    specifiedField: String,
    paginationQuery?: any
  ): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { administrativeDivision, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
