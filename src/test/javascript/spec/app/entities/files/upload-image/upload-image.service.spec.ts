/* tslint:disable max-line-length */
import axios from 'axios';
// import { format } from 'date-fns';
import moment, { Moment } from 'moment';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import UploadImageService from '@/entities/files/upload-image/upload-image.service';
import { UploadImage } from '@/shared/model/files/upload-image.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('UploadImage Service', () => {
    let service: UploadImageService;
    let elemDefault;
    let currentDate: Moment;
    beforeEach(() => {
      service = new UploadImageService();
      currentDate = moment(Date.now());

      elemDefault = new UploadImage(
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
        'AAAAAAA',
        'AAAAAAA',
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
      it('should create a UploadImage', async () => {
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

      it('should update a UploadImage', async () => {
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
            smartUrl: 'BBBBBB',
            mediumUrl: 'BBBBBB',
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
      it('should return a list of UploadImage', async () => {
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
            smartUrl: 'BBBBBB',
            mediumUrl: 'BBBBBB',
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
      it('should delete a UploadImage', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
