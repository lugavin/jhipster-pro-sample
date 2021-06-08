import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IDataDictionary } from '@/shared/model/settings/data-dictionary.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/data-dictionaries';
type EntityResponseType = AxiosResponse<IDataDictionary>;
type EntityArrayResponseType = AxiosResponse<IDataDictionary[]>;

export default class DataDictionaryService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IDataDictionary>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IDataDictionary[]>(baseApiUrl, {
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

  public create(entity: IDataDictionary): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: IDataDictionary): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(dataDictionary: IDataDictionary, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { dataDictionary, specifiedFields });
  }

  updateBySpecifiedField(dataDictionary: IDataDictionary, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { dataDictionary, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
