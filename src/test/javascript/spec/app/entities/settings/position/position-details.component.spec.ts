/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import PositionDetailComponent from '@/entities/settings/position/position-details.vue';
import PositionClass from '@/entities/settings/position/position-details.component';
import PositionService from '@/entities/settings/position/position.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Position Management Detail Component', () => {
    let wrapper: Wrapper<PositionClass>;
    let comp: PositionClass;
    let positionServiceStub: SinonStubbedInstance<PositionService>;

    beforeEach(() => {
      positionServiceStub = sinon.createStubInstance<PositionService>(PositionService);

      wrapper = shallowMount<PositionClass>(PositionDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { positionService: () => positionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPosition = { id: 123 };
        // @ts-ignore
        positionServiceStub.find.resolves(foundPosition);

        // WHEN
        comp.retrievePosition(123);
        await comp.$nextTick();

        // THEN
        expect(comp.position).toBe(foundPosition);
      });
    });
  });
});
