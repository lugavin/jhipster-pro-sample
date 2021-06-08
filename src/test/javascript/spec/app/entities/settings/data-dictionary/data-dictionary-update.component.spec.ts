/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import DataDictionaryUpdateComponent from '@/entities/settings/data-dictionary/data-dictionary-update.vue';
import DataDictionaryClass from '@/entities/settings/data-dictionary/data-dictionary-update.component';
import DataDictionaryService from '@/entities/settings/data-dictionary/data-dictionary.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('DataDictionary Management Update Component', () => {
    let wrapper: Wrapper<DataDictionaryClass>;
    let comp: DataDictionaryClass;
    let dataDictionaryServiceStub: SinonStubbedInstance<DataDictionaryService>;

    beforeEach(() => {
      dataDictionaryServiceStub = sinon.createStubInstance<DataDictionaryService>(DataDictionaryService);

      wrapper = shallowMount<DataDictionaryClass>(DataDictionaryUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          dataDictionaryService: () => dataDictionaryServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.dataDictionary = entity;
        // @ts-ignore
        dataDictionaryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataDictionaryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dataDictionary = entity;
        // @ts-ignore
        dataDictionaryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataDictionaryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
