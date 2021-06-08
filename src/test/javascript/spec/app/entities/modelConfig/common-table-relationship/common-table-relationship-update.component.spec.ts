/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import CommonTableRelationshipUpdateComponent from '@/entities/modelConfig/common-table-relationship/common-table-relationship-update.vue';
import CommonTableRelationshipClass from '@/entities/modelConfig/common-table-relationship/common-table-relationship-update.component';
import CommonTableRelationshipService from '@/entities/modelConfig/common-table-relationship/common-table-relationship.service';

import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

import DataDictionaryService from '@/entities/settings/data-dictionary/data-dictionary.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('CommonTableRelationship Management Update Component', () => {
    let wrapper: Wrapper<CommonTableRelationshipClass>;
    let comp: CommonTableRelationshipClass;
    let commonTableRelationshipServiceStub: SinonStubbedInstance<CommonTableRelationshipService>;

    beforeEach(() => {
      commonTableRelationshipServiceStub = sinon.createStubInstance<CommonTableRelationshipService>(CommonTableRelationshipService);

      wrapper = shallowMount<CommonTableRelationshipClass>(CommonTableRelationshipUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          commonTableRelationshipService: () => commonTableRelationshipServiceStub,

          commonTableService: () => new CommonTableService(),

          dataDictionaryService: () => new DataDictionaryService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.commonTableRelationship = entity;
        // @ts-ignore
        commonTableRelationshipServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableRelationshipServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.commonTableRelationship = entity;
        // @ts-ignore
        commonTableRelationshipServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(commonTableRelationshipServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
