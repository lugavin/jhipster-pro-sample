/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import GpsInfoUpdateComponent from '@/entities/settings/gps-info/gps-info-update.vue';
import GpsInfoClass from '@/entities/settings/gps-info/gps-info-update.component';
import GpsInfoService from '@/entities/settings/gps-info/gps-info.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('GpsInfo Management Update Component', () => {
    let wrapper: Wrapper<GpsInfoClass>;
    let comp: GpsInfoClass;
    let gpsInfoServiceStub: SinonStubbedInstance<GpsInfoService>;

    beforeEach(() => {
      gpsInfoServiceStub = sinon.createStubInstance<GpsInfoService>(GpsInfoService);

      wrapper = shallowMount<GpsInfoClass>(GpsInfoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          gpsInfoService: () => gpsInfoServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.gpsInfo = entity;
        // @ts-ignore
        gpsInfoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(gpsInfoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.gpsInfo = entity;
        // @ts-ignore
        gpsInfoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(gpsInfoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
