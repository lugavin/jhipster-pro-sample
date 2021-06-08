/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import AdministrativeDivisionDetailComponent from '@/entities/settings/administrative-division/administrative-division-details.vue';
import AdministrativeDivisionClass from '@/entities/settings/administrative-division/administrative-division-details.component';
import AdministrativeDivisionService from '@/entities/settings/administrative-division/administrative-division.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('AdministrativeDivision Management Detail Component', () => {
    let wrapper: Wrapper<AdministrativeDivisionClass>;
    let comp: AdministrativeDivisionClass;
    let administrativeDivisionServiceStub: SinonStubbedInstance<AdministrativeDivisionService>;

    beforeEach(() => {
      administrativeDivisionServiceStub = sinon.createStubInstance<AdministrativeDivisionService>(AdministrativeDivisionService);

      wrapper = shallowMount<AdministrativeDivisionClass>(AdministrativeDivisionDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { administrativeDivisionService: () => administrativeDivisionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAdministrativeDivision = { id: 123 };
        // @ts-ignore
        administrativeDivisionServiceStub.find.resolves(foundAdministrativeDivision);

        // WHEN
        comp.retrieveAdministrativeDivision(123);
        await comp.$nextTick();

        // THEN
        expect(comp.administrativeDivision).toBe(foundAdministrativeDivision);
      });
    });
  });
});
