/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import UploadFileService from '@/entities/files/upload-file/upload-file.service';
import { UploadFile } from '@/shared/model/files/upload-file.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('UploadFile Service', () => {
    let service: UploadFileService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new UploadFileService();
      currentDate = moment(Date.now());

      elemDefault = new UploadFile(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        0,
        0
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a UploadFile', async () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a UploadFile', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            name: 'BBBBBB',
            ext: 'BBBBBB',
            type: 'BBBBBB',
            url: 'BBBBBB',
            path: 'BBBBBB',
            folder: 'BBBBBB',
            entityName: 'BBBBBB',
            createAt: currentDate,
            fileSize: 1,
            referenceCount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of UploadFile', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            name: 'BBBBBB',
            ext: 'BBBBBB',
            type: 'BBBBBB',
            url: 'BBBBBB',
            path: 'BBBBBB',
            folder: 'BBBBBB',
            entityName: 'BBBBBB',
            createAt: currentDate,
            fileSize: 1,
            referenceCount: 1,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createAt: currentDate,
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a UploadFile', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
