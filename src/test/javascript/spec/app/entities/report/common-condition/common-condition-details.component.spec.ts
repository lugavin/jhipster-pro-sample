/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommonConditionDetailComponent from '@/entities/report/common-condition/common-condition-details.vue';
import CommonConditionClass from '@/entities/report/common-condition/common-condition-details.component';
import CommonConditionService from '@/entities/report/common-condition/common-condition.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CommonCondition Management Detail Component', () => {
    let wrapper: Wrapper<CommonConditionClass>;
    let comp: CommonConditionClass;
    let commonConditionServiceStub: SinonStubbedInstance<CommonConditionService>;

    beforeEach(() => {
      commonConditionServiceStub = sinon.createStubInstance<CommonConditionService>(CommonConditionService);

      wrapper = shallowMount<CommonConditionClass>(CommonConditionDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { commonConditionService: () => commonConditionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCommonCondition = { id: 123 };
        // @ts-ignore
        commonConditionServiceStub.find.resolves(foundCommonCondition);

        // WHEN
        comp.retrieveCommonCondition(123);
        await comp.$nextTick();

        // THEN
        expect(comp.commonCondition).toBe(foundCommonCondition);
      });
    });
  });
});
