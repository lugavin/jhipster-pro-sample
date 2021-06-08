/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import SmsConfigService from '@/entities/files/sms-config/sms-config.service';
import { SmsConfig } from '@/shared/model/files/sms-config.model';
import { SmsProvider } from '@/shared/model/enumerations/sms-provider.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('SmsConfig Service', () => {
    let service: SmsConfigService;
    let elemDefault;
    beforeEach(() => {
      service = new SmsConfigService();

      elemDefault = new SmsConfig(
        0,
        SmsProvider.YUNPIAN,
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
      it('should create a SmsConfig', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a SmsConfig', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provider: 'BBBBBB',
            smsCode: 'BBBBBB',
            templateId: 'BBBBBB',
            accessKey: 'BBBBBB',
            secretKey: 'BBBBBB',
            regionId: 'BBBBBB',
            signName: 'BBBBBB',
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
      it('should return a list of SmsConfig', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provider: 'BBBBBB',
            smsCode: 'BBBBBB',
            templateId: 'BBBBBB',
            accessKey: 'BBBBBB',
            secretKey: 'BBBBBB',
            regionId: 'BBBBBB',
            signName: 'BBBBBB',
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
      it('should delete a SmsConfig', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
