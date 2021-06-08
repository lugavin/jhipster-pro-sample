import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/common-tables';
type EntityResponseType = AxiosResponse<ICommonTable>;
type EntityArrayResponseType = AxiosResponse<ICommonTable[]>;

export default class CommonTableService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<ICommonTable>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<ICommonTable[]>(baseApiUrl, {
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

  public create(entity: ICommonTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: ICommonTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public copy(copyId: number): Observable<EntityResponseType> {
    return Axios.put(`${baseApiUrl}/copy/${copyId}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(commonTable: ICommonTable, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commonTable);
    return Axios.put(`${baseApiUrl}/specified-fields`, { commonTable: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(commonTable: ICommonTable, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(commonTable);
    return Axios.put(`${baseApiUrl}/specified-field`, { commonTable: copy, specifiedField });
  }

  protected convertDateFromClient(commonTable: ICommonTable): ICommonTable {
    const copy: ICommonTable = Object.assign({}, commonTable, {
      creatAt: commonTable.creatAt != null && commonTable.creatAt.isValid() ? commonTable.creatAt.toJSON() : null,
      generateAt: commonTable.generateAt != null && commonTable.generateAt.isValid() ? commonTable.generateAt.toJSON() : null,
      generateClassAt:
        commonTable.generateClassAt != null && commonTable.generateClassAt.isValid() ? commonTable.generateClassAt.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.creatAt = res.data.creatAt != null ? moment(res.data.creatAt) : null;
      res.data.generateAt = res.data.generateAt != null ? moment(res.data.generateAt) : null;
      res.data.generateClassAt = res.data.generateClassAt != null ? moment(res.data.generateClassAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((commonTable: ICommonTable) => {
        commonTable.creatAt = commonTable.creatAt != null ? moment(commonTable.creatAt) : null;
        commonTable.generateAt = commonTable.generateAt != null ? moment(commonTable.generateAt) : null;
        commonTable.generateClassAt = commonTable.generateClassAt != null ? moment(commonTable.generateClassAt) : null;
      });
    }
    return res;
  }

  public findByEntityName(entityName: string): Observable<EntityResponseType> {
    return Axios.get<ICommonTable>(`${baseApiUrl}/entity-name/${entityName}`).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }
  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
