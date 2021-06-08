/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import GpsInfoDetailComponent from '@/entities/settings/gps-info/gps-info-details.vue';
import GpsInfoClass from '@/entities/settings/gps-info/gps-info-details.component';
import GpsInfoService from '@/entities/settings/gps-info/gps-info.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('GpsInfo Management Detail Component', () => {
    let wrapper: Wrapper<GpsInfoClass>;
    let comp: GpsInfoClass;
    let gpsInfoServiceStub: SinonStubbedInstance<GpsInfoService>;

    beforeEach(() => {
      gpsInfoServiceStub = sinon.createStubInstance<GpsInfoService>(GpsInfoService);

      wrapper = shallowMount<GpsInfoClass>(GpsInfoDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { gpsInfoService: () => gpsInfoServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGpsInfo = { id: 123 };
        // @ts-ignore
        gpsInfoServiceStub.find.resolves(foundGpsInfo);

        // WHEN
        comp.retrieveGpsInfo(123);
        await comp.$nextTick();

        // THEN
        expect(comp.gpsInfo).toBe(foundGpsInfo);
      });
    });
  });
});
