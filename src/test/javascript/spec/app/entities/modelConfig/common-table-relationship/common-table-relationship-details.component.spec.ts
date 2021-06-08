/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CommonTableRelationshipDetailComponent from '@/entities/modelConfig/common-table-relationship/common-table-relationship-details.vue';
import CommonTableRelationshipClass from '@/entities/modelConfig/common-table-relationship/common-table-relationship-details.component';
import CommonTableRelationshipService from '@/entities/modelConfig/common-table-relationship/common-table-relationship.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CommonTableRelationship Management Detail Component', () => {
    let wrapper: Wrapper<CommonTableRelationshipClass>;
    let comp: CommonTableRelationshipClass;
    let commonTableRelationshipServiceStub: SinonStubbedInstance<CommonTableRelationshipService>;

    beforeEach(() => {
      commonTableRelationshipServiceStub = sinon.createStubInstance<CommonTableRelationshipService>(CommonTableRelationshipService);

      wrapper = shallowMount<CommonTableRelationshipClass>(CommonTableRelationshipDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { commonTableRelationshipService: () => commonTableRelationshipServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCommonTableRelationship = { id: 123 };
        // @ts-ignore
        commonTableRelationshipServiceStub.find.resolves(foundCommonTableRelationship);

        // WHEN
        comp.retrieveCommonTableRelationship(123);
        await comp.$nextTick();

        // THEN
        expect(comp.commonTableRelationship).toBe(foundCommonTableRelationship);
      });
    });
  });
});
