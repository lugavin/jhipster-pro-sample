/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import CommonTableUpdateComponent from '@/entities/modelConfig/common-table/common-table-update.vue';
import CommonTableClass from '@/entities/modelConfig/common-table/common-table-update.component';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

import CommonTableFieldService from '@/entities/modelConfig/common-table-field/common-table-field.service';

import CommonTableRelationshipService from '@/entities/modelConfig/common-table-relationship/common-table-relationship.service';

import UserService from '@/admin/user-management/user-management.service';

import BusinessTypeService from '@/entities/settings/business-type/business-type.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('CommonTable Management Update Component', () => {
    let wrapper: Wrapper<CommonTableClass>;
    let comp: CommonTableClass;
    let commonTableServiceStub: SinonStubbedInstance<CommonTableService>;

    beforeEach(() => {
      commonTableServiceStub = sinon.createStubInstance<CommonTableService>(CommonTableService);

      wrapper = shallowMount<CommonTableClass>(CommonTableUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          commonTableService: () => commonTableServiceStub,

          commonTableFieldService: () => new CommonTableFieldService(),

          commonTableRelationshipService: () => new CommonTableRelationshipService(),

          userService: () => new UserService(),

          businessTypeService: () => new BusinessTypeService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.commonTable = entity;
        // @ts-ignore
        commonTableServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.commonTable = entity;
        // @ts-ignore
        commonTableServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
