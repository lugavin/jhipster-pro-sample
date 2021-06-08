import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ICommonCondition } from '@/shared/model/report/common-condition.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/common-conditions';
type EntityResponseType = AxiosResponse<ICommonCondition>;
type EntityArrayResponseType = AxiosResponse<ICommonCondition[]>;

export default class CommonConditionService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<ICommonCondition>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<ICommonCondition[]>(baseApiUrl, {
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

  public create(entity: ICommonCondition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: ICommonCondition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(commonCondition: ICommonCondition, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commonCondition);
    return Axios.put(`${baseApiUrl}/specified-fields`, { commonCondition: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(commonCondition: ICommonCondition, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(commonCondition);
    return Axios.put(`${baseApiUrl}/specified-field`, { commonCondition: copy, specifiedField });
  }

  protected convertDateFromClient(commonCondition: ICommonCondition): ICommonCondition {
    const copy: ICommonCondition = Object.assign({}, commonCondition, {
      lastModifiedTime:
        commonCondition.lastModifiedTime != null && commonCondition.lastModifiedTime.isValid()
          ? commonCondition.lastModifiedTime.toJSON()
          : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.lastModifiedTime = res.data.lastModifiedTime != null ? moment(res.data.lastModifiedTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((commonCondition: ICommonCondition) => {
        commonCondition.lastModifiedTime = commonCondition.lastModifiedTime != null ? moment(commonCondition.lastModifiedTime) : null;
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
