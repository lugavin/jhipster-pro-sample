import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IApiPermission } from '@/shared/model/system/api-permission.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/api-permissions';
type EntityResponseType = AxiosResponse<IApiPermission>;
type EntityArrayResponseType = AxiosResponse<IApiPermission[]>;

export default class ApiPermissionService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IApiPermission>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IApiPermission[]>(baseApiUrl, {
      params: options,
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    });
  }

  public delete(id: number): Observable<AxiosResponse> {
    return Axios.delete(`${baseApiUrl}/${id}`);
  }

  public generate(): Observable<AxiosResponse> {
    return Axios.get(`${baseApiUrl}/generate`);
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

  public create(entity: IApiPermission): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: IApiPermission): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(apiPermission: IApiPermission, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { apiPermission, specifiedFields });
  }

  updateBySpecifiedField(apiPermission: IApiPermission, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { apiPermission, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
