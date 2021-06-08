import Axios from 'axios-observable';
import qs from 'qs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IUploadImage } from '@/shared/model/files/upload-image.model';
import { AxiosResponse } from 'axios';
import { Observable } from 'rxjs';

const baseApiUrl = 'api/upload-images';
type EntityResponseType = AxiosResponse<IUploadImage>;
type EntityArrayResponseType = AxiosResponse<IUploadImage[]>;

export default class UploadImageService {
  public find(id: number): Observable<EntityResponseType> {
    return Axios.get<IUploadImage>(`${baseApiUrl}/${id}`).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public retrieve(paginationQuery?: any): Observable<EntityArrayResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return Axios.get<IUploadImage[]>(baseApiUrl, {
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

  public create(entity: IUploadImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.post(`${baseApiUrl}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  public update(entity: IUploadImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return Axios.put(`${baseApiUrl}/${entity.id}`, copy).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uploadImage: IUploadImage, specifiedFields: String[]): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadImage);
    return Axios.put(`${baseApiUrl}/specified-fields`, { uploadImage: copy, specifiedFields }).pipe(
      map((res: EntityResponseType) => this.convertDateFromServer(res))
    );
  }

  updateBySpecifiedField(uploadImage: IUploadImage, specifiedField: String, paginationQuery?: any): Observable<EntityResponseType> {
    const options = buildPaginationQueryOpts(paginationQuery);
    const copy = this.convertDateFromClient(uploadImage);
    return Axios.put(`${baseApiUrl}/specified-field`, { uploadImage: copy, specifiedField });
  }

  protected convertDateFromClient(uploadImage: IUploadImage): IUploadImage {
    const copy: IUploadImage = Object.assign({}, uploadImage, {
      createAt: uploadImage.createAt != null && uploadImage.createAt.isValid() ? uploadImage.createAt.toJSON() : null,
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
      res.data.forEach((uploadImage: IUploadImage) => {
        uploadImage.createAt = uploadImage.createAt != null ? moment(uploadImage.createAt) : null;
      });
    }
    return res;
  }

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
}
