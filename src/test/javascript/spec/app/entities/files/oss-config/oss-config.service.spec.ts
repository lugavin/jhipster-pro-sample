/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import OssConfigService from '@/entities/files/oss-config/oss-config.service';
import { OssConfig } from '@/shared/model/files/oss-config.model';
import { OssProvider } from '@/shared/model/enumerations/oss-provider.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('OssConfig Service', () => {
    let service: OssConfigService;
    let elemDefault;
    beforeEach(() => {
      service = new OssConfigService();

      elemDefault = new OssConfig(
        0,
        OssProvider.MINIO,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a OssConfig', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a OssConfig', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provider: 'BBBBBB',
            ossCode: 'BBBBBB',
            endpoint: 'BBBBBB',
            accessKey: 'BBBBBB',
            secretKey: 'BBBBBB',
            bucketName: 'BBBBBB',
            appId: 'BBBBBB',
            region: 'BBBBBB',
            remark: 'BBBBBB',
            enabled: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of OssConfig', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provider: 'BBBBBB',
            ossCode: 'BBBBBB',
            endpoint: 'BBBBBB',
            accessKey: 'BBBBBB',
            secretKey: 'BBBBBB',
            bucketName: 'BBBBBB',
            appId: 'BBBBBB',
            region: 'BBBBBB',
            remark: 'BBBBBB',
            enabled: true,
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a OssConfig', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
