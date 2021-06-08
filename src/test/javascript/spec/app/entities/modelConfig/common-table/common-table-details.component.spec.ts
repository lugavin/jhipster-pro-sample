/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommonTableDetailComponent from '@/entities/modelConfig/common-table/common-table-details.vue';
import CommonTableClass from '@/entities/modelConfig/common-table/common-table-details.component';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CommonTable Management Detail Component', () => {
    let wrapper: Wrapper<CommonTableClass>;
    let comp: CommonTableClass;
    let commonTableServiceStub: SinonStubbedInstance<CommonTableService>;

    beforeEach(() => {
      commonTableServiceStub = sinon.createStubInstance<CommonTableService>(CommonTableService);

      wrapper = shallowMount<CommonTableClass>(CommonTableDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { commonTableService: () => commonTableServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCommonTable = { id: 123 };
        // @ts-ignore
        commonTableServiceStub.find.resolves(foundCommonTable);

        // WHEN
        comp.retrieveCommonTable(123);
        await comp.$nextTick();

        // THEN
        expect(comp.commonTable).toBe(foundCommonTable);
      });
    });
  });
});
