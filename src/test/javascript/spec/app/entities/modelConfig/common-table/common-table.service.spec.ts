/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import { CommonTable } from '@/shared/model/modelConfig/common-table.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('CommonTable Service', () => {
    let service: CommonTableService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new CommonTableService();
      currentDate = moment(Date.now());

      elemDefault = new CommonTable(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        'AAAAAAA',
        false,
        currentDate,
        currentDate,
        currentDate,
        'AAAAAAA',
        false,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        false
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a CommonTable', async () => {
        const returnedFromService = Object.assign(
          {
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a CommonTable', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            entityName: 'BBBBBB',
            tableName: 'BBBBBB',
            system: true,
            clazzName: 'BBBBBB',
            generated: true,
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
            description: 'BBBBBB',
            treeTable: true,
            baseTableId: 1,
            recordActionWidth: 1,
            listConfig: 'BBBBBB',
            formConfig: 'BBBBBB',
            editInModal: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of CommonTable', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            entityName: 'BBBBBB',
            tableName: 'BBBBBB',
            system: true,
            clazzName: 'BBBBBB',
            generated: true,
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
            description: 'BBBBBB',
            treeTable: true,
            baseTableId: 1,
            recordActionWidth: 1,
            listConfig: 'BBBBBB',
            formConfig: 'BBBBBB',
            editInModal: true,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            creatAt: currentDate,
            generateAt: currentDate,
            generateClassAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a CommonTable', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
