/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommonConditionItemDetailComponent from '@/entities/report/common-condition-item/common-condition-item-details.vue';
import CommonConditionItemClass from '@/entities/report/common-condition-item/common-condition-item-details.component';
import CommonConditionItemService from '@/entities/report/common-condition-item/common-condition-item.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CommonConditionItem Management Detail Component', () => {
    let wrapper: Wrapper<CommonConditionItemClass>;
    let comp: CommonConditionItemClass;
    let commonConditionItemServiceStub: SinonStubbedInstance<CommonConditionItemService>;

    beforeEach(() => {
      commonConditionItemServiceStub = sinon.createStubInstance<CommonConditionItemService>(CommonConditionItemService);

      wrapper = shallowMount<CommonConditionItemClass>(CommonConditionItemDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { commonConditionItemService: () => commonConditionItemServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCommonConditionItem = { id: 123 };
        // @ts-ignore
        commonConditionItemServiceStub.find.resolves(foundCommonConditionItem);

        // WHEN
        comp.retrieveCommonConditionItem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.commonConditionItem).toBe(foundCommonConditionItem);
      });
    });
  });
});
