import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IStatisticsApi } from '@/shared/model/report/statistics-api.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/statistics-apis';
type EntityResponseType = AxiosResponse<IStatisticsApi>;
type EntityArrayResponseType = AxiosResponse<IStatisticsApi[]>;

export default class StatisticsApiService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IStatisticsApi>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public result(id: number): Observable<any> {
    return Axios.get(`${baseApiUrl}/result/${id}`);
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IStatisticsApi[]>(baseApiUrl, {
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

  public create(entity: IStatisticsApi): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IStatisticsApi): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(statisticsApi: IStatisticsApi, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statisticsApi);
    return Axios.put(`${baseApiUrl}/specified-fields`, { statisticsApi: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(statisticsApi: IStatisticsApi, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(statisticsApi);
    return Axios.put(`${baseApiUrl}/specified-field`, { statisticsApi: copy, specifiedField });
  }

  protected convertDateFromClient(statisticsApi: IStatisticsApi): IStatisticsApi {
    const copy: IStatisticsApi = Object.assign({}, statisticsApi, {
      createAt: statisticsApi.createAt != null && statisticsApi.createAt.isValid() ? statisticsApi.createAt.toJSON() : null,
      updateAt: statisticsApi.updateAt != null && statisticsApi.updateAt.isValid() ? statisticsApi.updateAt.toJSON() : null,
      lastSQLRunTime:
        statisticsApi.lastSQLRunTime != null && statisticsApi.lastSQLRunTime.isValid() ? statisticsApi.lastSQLRunTime.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.createAt = res.data.createAt != null ? moment(res.data.createAt) : null;
      res.data.updateAt = res.data.updateAt != null ? moment(res.data.updateAt) : null;
      res.data.lastSQLRunTime = res.data.lastSQLRunTime != null ? moment(res.data.lastSQLRunTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((statisticsApi: IStatisticsApi) => {
        statisticsApi.createAt = statisticsApi.createAt != null ? moment(statisticsApi.createAt) : null;
        statisticsApi.updateAt = statisticsApi.updateAt != null ? moment(statisticsApi.updateAt) : null;
        statisticsApi.lastSQLRunTime = statisticsApi.lastSQLRunTime != null ? moment(statisticsApi.lastSQLRunTime) : null;
      });
    }
    return res;
  }
  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
