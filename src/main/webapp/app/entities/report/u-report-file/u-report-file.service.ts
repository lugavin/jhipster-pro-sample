import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IUReportFile } from '@/shared/model/report/u-report-file.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/u-report-files';
type EntityResponseType = AxiosResponse<IUReportFile>;
type EntityArrayResponseType = AxiosResponse<IUReportFile[]>;

export default class UReportFileService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IUReportFile>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IUReportFile[]>(baseApiUrl, {
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

  public create(entity: IUReportFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IUReportFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uReportFile: IUReportFile, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uReportFile);
    return Axios.put(`${baseApiUrl}/specified-fields`, { uReportFile: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(uReportFile: IUReportFile, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(uReportFile);
    return Axios.put(`${baseApiUrl}/specified-field`, { uReportFile: copy, specifiedField });
  }

  protected convertDateFromClient(uReportFile: IUReportFile): IUReportFile {
    const copy: IUReportFile = Object.assign({}, uReportFile, {
      createAt: uReportFile.createAt != null && uReportFile.createAt.isValid() ? uReportFile.createAt.toJSON() : null,
      updateAt: uReportFile.updateAt != null && uReportFile.updateAt.isValid() ? uReportFile.updateAt.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.createAt = res.data.createAt != null ? moment(res.data.createAt) : null;
      res.data.updateAt = res.data.updateAt != null ? moment(res.data.updateAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((uReportFile: IUReportFile) => {
        uReportFile.createAt = uReportFile.createAt != null ? moment(uReportFile.createAt) : null;
        uReportFile.updateAt = uReportFile.updateAt != null ? moment(uReportFile.updateAt) : null;
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
