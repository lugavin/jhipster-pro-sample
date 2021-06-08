/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommonTableFieldDetailComponent from '@/entities/modelConfig/common-table-field/common-table-field-details.vue';
import CommonTableFieldClass from '@/entities/modelConfig/common-table-field/common-table-field-details.component';
import CommonTableFieldService from '@/entities/modelConfig/common-table-field/common-table-field.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CommonTableField Management Detail Component', () => {
    let wrapper: Wrapper<CommonTableFieldClass>;
    let comp: CommonTableFieldClass;
    let commonTableFieldServiceStub: SinonStubbedInstance<CommonTableFieldService>;

    beforeEach(() => {
      commonTableFieldServiceStub = sinon.createStubInstance<CommonTableFieldService>(CommonTableFieldService);

      wrapper = shallowMount<CommonTableFieldClass>(CommonTableFieldDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { commonTableFieldService: () => commonTableFieldServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCommonTableField = { id: 123 };
        // @ts-ignore
        commonTableFieldServiceStub.find.resolves(foundCommonTableField);

        // WHEN
        comp.retrieveCommonTableField(123);
        await comp.$nextTick();

        // THEN
        expect(comp.commonTableField).toBe(foundCommonTableField);
      });
    });
  });
});
