/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import CommonConditionUpdateComponent from '@/entities/report/common-condition/common-condition-update.vue';
import CommonConditionClass from '@/entities/report/common-condition/common-condition-update.component';
import CommonConditionService from '@/entities/report/common-condition/common-condition.service';

import CommonConditionItemService from '@/entities/report/common-condition-item/common-condition-item.service';

import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('CommonCondition Management Update Component', () => {
    let wrapper: Wrapper<CommonConditionClass>;
    let comp: CommonConditionClass;
    let commonConditionServiceStub: SinonStubbedInstance<CommonConditionService>;

    beforeEach(() => {
      commonConditionServiceStub = sinon.createStubInstance<CommonConditionService>(CommonConditionService);

      wrapper = shallowMount<CommonConditionClass>(CommonConditionUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          commonConditionService: () => commonConditionServiceStub,

          commonConditionItemService: () => new CommonConditionItemService(),

          commonTableService: () => new CommonTableService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.commonCondition = entity;
        // @ts-ignore
        commonConditionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonConditionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.commonCondition = entity;
        // @ts-ignore
        commonConditionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonConditionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
