/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import CommonConditionItemService from '@/entities/report/common-condition-item/common-condition-item.service';
import { CommonConditionItem } from '@/shared/model/report/common-condition-item.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('CommonConditionItem Service', () => {
    let service: CommonConditionItemService;
    let elemDefault;
    beforeEach(() => {
      service = new CommonConditionItemService();

      elemDefault = new CommonConditionItem(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).subscribe(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });
      it('should create a CommonConditionItem', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a CommonConditionItem', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prefix: 'BBBBBB',
            fieldName: 'BBBBBB',
            fieldType: 'BBBBBB',
            operator: 'BBBBBB',
            value: 'BBBBBB',
            suffix: 'BBBBBB',
            order: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of CommonConditionItem', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prefix: 'BBBBBB',
            fieldName: 'BBBBBB',
            fieldType: 'BBBBBB',
            operator: 'BBBBBB',
            value: 'BBBBBB',
            suffix: 'BBBBBB',
            order: 1,
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a CommonConditionItem', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
