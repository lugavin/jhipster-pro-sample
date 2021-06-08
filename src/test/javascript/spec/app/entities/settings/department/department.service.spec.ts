/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import DepartmentService from '@/entities/settings/department/department.service';
import { Department } from '@/shared/model/settings/department.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('Department Service', () => {
    let service: DepartmentService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new DepartmentService();
      currentDate = moment(Date.now());

      elemDefault = new Department(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createTime: currentDate,
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a Department', async () => {
        const returnedFromService = Object.assign(
          {
            createTime: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a Department', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            code: 'BBBBBB',
            address: 'BBBBBB',
            phoneNum: 'BBBBBB',
            logo: 'BBBBBB',
            contact: 'BBBBBB',
            createUserId: 1,
            createTime: currentDate,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of Department', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            code: 'BBBBBB',
            address: 'BBBBBB',
            phoneNum: 'BBBBBB',
            logo: 'BBBBBB',
            contact: 'BBBBBB',
            createUserId: 1,
            createTime: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a Department', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
