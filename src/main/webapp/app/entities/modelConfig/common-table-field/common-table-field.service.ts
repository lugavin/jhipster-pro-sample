import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ICommonTableField } from '@/shared/model/modelConfig/common-table-field.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/common-table-fields';
type EntityResponseType = AxiosResponse<ICommonTableField>;
type EntityArrayResponseType = AxiosResponse<ICommonTableField[]>;

export default class CommonTableFieldService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<ICommonTableField>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<ICommonTableField[]>(baseApiUrl, {
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

  public create(entity: ICommonTableField): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: ICommonTableField): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(commonTableField: ICommonTableField, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { commonTableField, specifiedFields });
  }

  updateBySpecifiedField(
    commonTableField: ICommonTableField,
    specifiedField: String,
    paginationQuery?: any
  ): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { commonTableField, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
