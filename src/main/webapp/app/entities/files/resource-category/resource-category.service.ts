import Axios from 'axios-observable';
import qs from 'qs';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IResourceCategory } from '@/shared/model/files/resource-category.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/resource-categories';
type EntityResponseType = AxiosResponse<IResourceCategory>;
type EntityArrayResponseType = AxiosResponse<IResourceCategory[]>;

export default class ResourceCategoryService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IResourceCategory>(`${baseApiUrl}/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IResourceCategory[]>(baseApiUrl, {
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

  public create(entity: IResourceCategory): Observable<EntityResponseType> {
    return Axios.post(`${baseApiUrl}`, entity);
  }

  public update(entity: IResourceCategory): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${entity.id}`, entity);
  }

  updateBySpecifiedFields(resourceCategory: IResourceCategory, specifiedFields: String[]): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/specified-fields`, { resourceCategory, specifiedFields });
  }

  updateBySpecifiedField(
    resourceCategory: IResourceCategory,
    specifiedField: String,
    paginationQuery?: any
  ): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.put(`${baseApiUrl}/specified-field`, { resourceCategory, specifiedField });
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
