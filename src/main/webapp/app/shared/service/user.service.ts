import Axios from 'axios-observable';
import qs from 'qs';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { IUser } from '@/shared/model/user.model';
const baseApiUrl = 'api/admin/users';
type EntityResponseType = AxiosResponse<IUser>;
type EntityArrayResponseType = AxiosResponse<IUser[]>;

export default class UserService {
  public get(userId: number): Observable<EntityResponseType> {
    return Axios.get<IUser>(`${baseApiUrl}/${userId}`);
  }

  public create(user): Observable<EntityResponseType> {
    return Axios.post<IUser>(`${baseApiUrl}`, user);
  }

  public update(user): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/${user.id}`, user);
  }

  public remove(userId: number): Observable<AxiosResponse> {
    return Axios.delete(`${baseApiUrl}/${userId}`);
  }

  public deleteByIds(ids: string[]): Observable<AxiosResponse> {
    return Axios.delete(`${baseApiUrl}`, {
      params: { ids },
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    });
  }

  public retrieve(req?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(req);
    return Axios.get(baseApiUrl, {
      params: options,
      paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' });
      },
    });
  }

  public retrieveAuthorities(): Observable<AxiosResponse> {
    return Axios.get('api/users/authorities');
  }
}
