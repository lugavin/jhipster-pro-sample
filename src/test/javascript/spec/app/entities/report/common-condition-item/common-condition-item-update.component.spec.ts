/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import CommonConditionItemUpdateComponent from '@/entities/report/common-condition-item/common-condition-item-update.vue';
import CommonConditionItemClass from '@/entities/report/common-condition-item/common-condition-item-update.component';
import CommonConditionItemService from '@/entities/report/common-condition-item/common-condition-item.service';

import CommonConditionService from '@/entities/report/common-condition/common-condition.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('CommonConditionItem Management Update Component', () => {
    let wrapper: Wrapper<CommonConditionItemClass>;
    let comp: CommonConditionItemClass;
    let commonConditionItemServiceStub: SinonStubbedInstance<CommonConditionItemService>;

    beforeEach(() => {
      commonConditionItemServiceStub = sinon.createStubInstance<CommonConditionItemService>(CommonConditionItemService);

      wrapper = shallowMount<CommonConditionItemClass>(CommonConditionItemUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          commonConditionItemService: () => commonConditionItemServiceStub,

          commonConditionService: () => new CommonConditionService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.commonConditionItem = entity;
        // @ts-ignore
        commonConditionItemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonConditionItemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.commonConditionItem = entity;
        // @ts-ignore
        commonConditionItemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonConditionItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
