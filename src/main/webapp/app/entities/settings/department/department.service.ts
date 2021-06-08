import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IDepartment } from '@/shared/model/settings/department.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/departments';
type EntityResponseType = AxiosResponse<IDepartment>;
type EntityArrayResponseType = AxiosResponse<IDepartment[]>;

export default class DepartmentService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IDepartment>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IDepartment[]>(baseApiUrl, {
      params: options,
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    }).pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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
    return Axios.get(`${baseApiUrl}/tree`).pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  treeByParentId(parentId: number): Observable<EntityArrayResponseType> {
    return Axios.get(`${baseApiUrl}/${parentId}/tree`);
  }

  public create(entity: IDepartment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IDepartment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(department: IDepartment, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(department);
    return Axios.put(`${baseApiUrl}/specified-fields`, { department: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(department: IDepartment, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(department);
    return Axios.put(`${baseApiUrl}/specified-field`, { department: copy, specifiedField });
  }

  protected convertDateFromClient(department: IDepartment): IDepartment {
    const copy: IDepartment = Object.assign({}, department, {
      createTime: department.createTime != null && department.createTime.isValid() ? department.createTime.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.createTime = res.data.createTime != null ? moment(res.data.createTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((department: IDepartment) => {
        department.createTime = department.createTime != null ? moment(department.createTime) : null;
        if (department.children && department.children.length > 0) {
          const children = Object.assign({}, res, { data: department.children });
          department.children = this.convertDateArrayFromServer(children).data;
        }
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
