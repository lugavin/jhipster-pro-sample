/* tslint:disable max-line-length */
import axios from 'axios';

import * as config from '@/shared/config/config';
import {} from '@/shared/date/filters';
import ViewPermissionService from '@/entities/system/view-permission/view-permission.service';
import { ViewPermission } from '@/shared/model/system/view-permission.model';
import { TargetType } from '@/shared/model/enumerations/target-type.model';
import { ViewPermissionType } from '@/shared/model/enumerations/view-permission-type.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn(),
}));

describe('Service Tests', () => {
  describe('ViewPermission Service', () => {
    let service: ViewPermissionService;
    let elemDefault;
    beforeEach(() => {
      service = new ViewPermissionService();

      elemDefault = new ViewPermission(
        0,
        'AAAAAAA',
        'AAAAAAA',
        false,
        'AAAAAAA',
        'AAAAAAA',
        TargetType.BLANK,
        'AAAAAAA',
        false,
        false,
        false,
        false,
        false,
        false,
        'AAAAAAA',
        'AAAAAAA',
        ViewPermissionType.MENU,
        0,
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
      it('should create a ViewPermission', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a ViewPermission', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            i18n: 'BBBBBB',
            group: true,
            link: 'BBBBBB',
            externalLink: 'BBBBBB',
            target: 'BBBBBB',
            icon: 'BBBBBB',
            disabled: true,
            hide: true,
            hideInBreadcrumb: true,
            shortcut: true,
            shortcutRoot: true,
            reuse: true,
            code: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            order: 1,
            apiPermissionCodes: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).subscribe(res => {
          expect(res).toMatchObject(expected);
        });
      });
      it('should return a list of ViewPermission', async () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            i18n: 'BBBBBB',
            group: true,
            link: 'BBBBBB',
            externalLink: 'BBBBBB',
            target: 'BBBBBB',
            icon: 'BBBBBB',
            disabled: true,
            hide: true,
            hideInBreadcrumb: true,
            shortcut: true,
            shortcutRoot: true,
            reuse: true,
            code: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            order: 1,
            apiPermissionCodes: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve({ sort: {}, page: 0, size: 10 }).subscribe(res => {
          expect(res).toContainEqual(expected);
        });
      });
      it('should delete a ViewPermission', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).subscribe(res => {
          expect(res.data.ok).toBeTruthy();
        });
      });
    });
  });
});
