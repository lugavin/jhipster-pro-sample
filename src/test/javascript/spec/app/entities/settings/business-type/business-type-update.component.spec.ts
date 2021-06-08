/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BusinessTypeUpdateComponent from '@/entities/settings/business-type/business-type-update.vue';
import BusinessTypeClass from '@/entities/settings/business-type/business-type-update.component';
import BusinessTypeService from '@/entities/settings/business-type/business-type.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('BusinessType Management Update Component', () => {
    let wrapper: Wrapper<BusinessTypeClass>;
    let comp: BusinessTypeClass;
    let businessTypeServiceStub: SinonStubbedInstance<BusinessTypeService>;

    beforeEach(() => {
      businessTypeServiceStub = sinon.createStubInstance<BusinessTypeService>(BusinessTypeService);

      wrapper = shallowMount<BusinessTypeClass>(BusinessTypeUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          businessTypeService: () => businessTypeServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.businessType = entity;
        // @ts-ignore
        businessTypeServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(businessTypeServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.businessType = entity;
        // @ts-ignore
        businessTypeServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(businessTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
