/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import UReportFileService from '@/entities/report/u-report-file/u-report-file.service';
import { UReportFile } from '@/shared/model/report/u-report-file.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('UReportFile Service', () => {
    let service: UReportFileService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new UReportFileService();
      currentDate = moment(Date.now());

      elemDefault = new UReportFile(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a UReportFile', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a UReportFile', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            content: 'BBBBBB',
            createAt: currentDate,
            updateAt: currentDate,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of UReportFile', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            content: 'BBBBBB',
            createAt: currentDate,
            updateAt: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
            updateAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a UReportFile', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
