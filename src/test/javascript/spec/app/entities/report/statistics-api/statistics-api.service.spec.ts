/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import StatisticsApiService from '@/entities/report/statistics-api/statistics-api.service';
import { StatisticsApi } from '@/shared/model/report/statistics-api.model';
import { StatSourceType } from '@/shared/model/enumerations/stat-source-type.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('StatisticsApi Service', () => {
    let service: StatisticsApiService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new StatisticsApiService();
      currentDate = moment(Date.now());

      elemDefault = new StatisticsApi(
        0,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        StatSourceType.HTTP,
        'AAAAAAA',
        'AAAAAAA',
        0,
        currentDate,
        false
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
            lastSQLRunTime: currentDate,
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a StatisticsApi', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
            lastSQLRunTime: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
            lastSQLRunTime: currentDate,
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a StatisticsApi', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            apiKey: 'BBBBBB',
            createAt: currentDate,
            updateAt: currentDate,
            sourceType: 'BBBBBB',
            apiBody: 'BBBBBB',
            result: 'BBBBBB',
            updateInterval: 1,
            lastSQLRunTime: currentDate,
            enable: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
            lastSQLRunTime: currentDate,
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of StatisticsApi', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            apiKey: 'BBBBBB',
            createAt: currentDate,
            updateAt: currentDate,
            sourceType: 'BBBBBB',
            apiBody: 'BBBBBB',
            result: 'BBBBBB',
            updateInterval: 1,
            lastSQLRunTime: currentDate,
            enable: true,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
            lastSQLRunTime: currentDate,
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a StatisticsApi', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
