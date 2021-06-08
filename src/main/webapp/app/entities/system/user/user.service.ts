import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';
import { IUser } from '@/shared/model/user.model';

const baseApiUrl = 'api/admin/users';
type EntityResponseType = AxiosResponse<IUser>;
type EntityArrayResponseType = AxiosResponse<IUser[]>;

export default class UserService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IUser>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IUser[]>(baseApiUrl, {
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

  public create(entity: IUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(person: IUser, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return Axios.put(`${baseApiUrl}/specified-fields`, { person: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(person: IUser, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(person);
    return Axios.put(`${baseApiUrl}/specified-field`, { person: copy, specifiedField });
  }

  protected convertDateFromClient(user: IUser): IUser {
    const copy: IUser = Object.assign({}, user, {
      birthday: user.createdDate != null && user.createdDate.isValid() ? user.createdDate.toJSON() : null,
      lastModifiedDate: user.lastModifiedDate != null && user.lastModifiedDate.isValid() ? user.lastModifiedDate.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.createdDate = res.data.createdDate != null ? moment(res.data.createdDate) : null;
      res.data.lastModifiedDate = res.data.lastModifiedDate != null ? moment(res.data.lastModifiedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((user: IUser) => {
        user.createdDate = user.createdDate != null ? moment(user.createdDate) : null;
        user.lastModifiedDate = user.lastModifiedDate != null ? moment(user.lastModifiedDate) : null;
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
