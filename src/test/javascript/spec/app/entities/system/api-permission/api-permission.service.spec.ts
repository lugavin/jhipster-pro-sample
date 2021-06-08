/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import ApiPermissionService from '@/entities/system/api-permission/api-permission.service';
import { ApiPermission } from '@/shared/model/system/api-permission.model';
import { ApiPermissionType } from '@/shared/model/enumerations/api-permission-type.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('ApiPermission Service', () => {
    let service: ApiPermissionService;
    let elemDefault;
    beforeEach(() => {
      service = new ApiPermissionService();

      elemDefault = new ApiPermission(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', ApiPermissionType.BUSINESS, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a ApiPermission', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a ApiPermission', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            serviceName: 'BBBBBB',
            name: 'BBBBBB',
            code: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            method: 'BBBBBB',
            url: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of ApiPermission', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            serviceName: 'BBBBBB',
            name: 'BBBBBB',
            code: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            method: 'BBBBBB',
            url: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a ApiPermission', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
