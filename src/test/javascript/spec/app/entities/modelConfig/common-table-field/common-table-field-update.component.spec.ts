/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import CommonTableFieldUpdateComponent from '@/entities/modelConfig/common-table-field/common-table-field-update.vue';
import CommonTableFieldClass from '@/entities/modelConfig/common-table-field/common-table-field-update.component';
import CommonTableFieldService from '@/entities/modelConfig/common-table-field/common-table-field.service';

import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('CommonTableField Management Update Component', () => {
    let wrapper: Wrapper<CommonTableFieldClass>;
    let comp: CommonTableFieldClass;
    let commonTableFieldServiceStub: SinonStubbedInstance<CommonTableFieldService>;

    beforeEach(() => {
      commonTableFieldServiceStub = sinon.createStubInstance<CommonTableFieldService>(CommonTableFieldService);

      wrapper = shallowMount<CommonTableFieldClass>(CommonTableFieldUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          commonTableFieldService: () => commonTableFieldServiceStub,

          commonTableService: () => new CommonTableService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.commonTableField = entity;
        // @ts-ignore
        commonTableFieldServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableFieldServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.commonTableField = entity;
        // @ts-ignore
        commonTableFieldServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableFieldServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
