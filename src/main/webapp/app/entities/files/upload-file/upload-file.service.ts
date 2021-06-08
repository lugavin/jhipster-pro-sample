import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IUploadFile } from '@/shared/model/files/upload-file.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/upload-files';
type EntityResponseType = AxiosResponse<IUploadFile>;
type EntityArrayResponseType = AxiosResponse<IUploadFile[]>;

export default class UploadFileService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IUploadFile>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IUploadFile[]>(baseApiUrl, {
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

  public create(entity: IUploadFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IUploadFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uploadFile: IUploadFile, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadFile);
    return Axios.put(`${baseApiUrl}/specified-fields`, { uploadFile: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(uploadFile: IUploadFile, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(uploadFile);
    return Axios.put(`${baseApiUrl}/specified-field`, { uploadFile: copy, specifiedField });
  }

  protected convertDateFromClient(uploadFile: IUploadFile): IUploadFile {
    const copy: IUploadFile = Object.assign({}, uploadFile, {
      createAt: uploadFile.createAt != null && uploadFile.createAt.isValid() ? uploadFile.createAt.toJSON() : null,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.data) {
      res.data.createAt = res.data.createAt != null ? moment(res.data.createAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.data) {
      res.data.forEach((uploadFile: IUploadFile) => {
        uploadFile.createAt = uploadFile.createAt != null ? moment(uploadFile.createAt) : null;
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
