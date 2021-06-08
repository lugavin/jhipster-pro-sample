/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DataDictionaryDetailComponent from '@/entities/settings/data-dictionary/data-dictionary-details.vue';
import DataDictionaryClass from '@/entities/settings/data-dictionary/data-dictionary-details.component';
import DataDictionaryService from '@/entities/settings/data-dictionary/data-dictionary.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DataDictionary Management Detail Component', () => {
    let wrapper: Wrapper<DataDictionaryClass>;
    let comp: DataDictionaryClass;
    let dataDictionaryServiceStub: SinonStubbedInstance<DataDictionaryService>;

    beforeEach(() => {
      dataDictionaryServiceStub = sinon.createStubInstance<DataDictionaryService>(DataDictionaryService);

      wrapper = shallowMount<DataDictionaryClass>(DataDictionaryDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { dataDictionaryService: () => dataDictionaryServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDataDictionary = { id: 123 };
        // @ts-ignore
        dataDictionaryServiceStub.find.resolves(foundDataDictionary);

        // WHEN
        comp.retrieveDataDictionary(123);
        await comp.$nextTick();

        // THEN
        expect(comp.dataDictionary).toBe(foundDataDictionary);
      });
    });
  });
});
