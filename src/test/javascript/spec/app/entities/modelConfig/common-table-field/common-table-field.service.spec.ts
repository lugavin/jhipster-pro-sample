/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import CommonTableFieldService from '@/entities/modelConfig/common-table-field/common-table-field.service';
import { CommonTableField } from '@/shared/model/modelConfig/common-table-field.model';
import { CommonFieldType } from '@/shared/model/enumerations/common-field-type.model';
import { FixedType } from '@/shared/model/enumerations/fixed-type.model';
import { EndUsedType } from '@/shared/model/enumerations/end-used-type.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('CommonTableField Service', () => {
    let service: CommonTableFieldService;
    let elemDefault;
    beforeEach(() => {
      service = new CommonTableFieldService();

      elemDefault = new CommonTableField(
        0,
        'AAAAAAA',
        'AAAAAAA',
        CommonFieldType.INTEGER,
        'AAAAAAA',
        0,
        0,
        false,
        false,
        false,
        false,
        'AAAAAAA',
        false,
        FixedType.LEFT,
        false,
        false,
        false,
        'AAAAAAA',
        false,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        EndUsedType.FILE_URL,
        'AAAAAAA'
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
      it('should create a CommonTableField', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a CommonTableField', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            entityFieldName: 'BBBBBB',
            type: 'BBBBBB',
            tableColumnName: 'BBBBBB',
            columnWidth: 1,
            order: 1,
            editInList: true,
            hideInList: true,
            hideInForm: true,
            enableFilter: true,
            validateRules: 'BBBBBB',
            showInFilterTree: true,
            fixed: 'BBBBBB',
            sortable: true,
            treeIndicator: true,
            clientReadOnly: true,
            fieldValues: 'BBBBBB',
            notNull: true,
            system: true,
            help: 'BBBBBB',
            fontColor: 'BBBBBB',
            backgroundColor: 'BBBBBB',
            nullHideInForm: true,
            endUsed: 'BBBBBB',
            options: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of CommonTableField', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            entityFieldName: 'BBBBBB',
            type: 'BBBBBB',
            tableColumnName: 'BBBBBB',
            columnWidth: 1,
            order: 1,
            editInList: true,
            hideInList: true,
            hideInForm: true,
            enableFilter: true,
            validateRules: 'BBBBBB',
            showInFilterTree: true,
            fixed: 'BBBBBB',
            sortable: true,
            treeIndicator: true,
            clientReadOnly: true,
            fieldValues: 'BBBBBB',
            notNull: true,
            system: true,
            help: 'BBBBBB',
            fontColor: 'BBBBBB',
            backgroundColor: 'BBBBBB',
            nullHideInForm: true,
            endUsed: 'BBBBBB',
            options: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a CommonTableField', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
