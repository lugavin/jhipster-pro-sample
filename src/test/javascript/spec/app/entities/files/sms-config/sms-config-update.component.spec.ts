/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SmsConfigUpdateComponent from '@/entities/files/sms-config/sms-config-update.vue';
import SmsConfigClass from '@/entities/files/sms-config/sms-config-update.component';
import SmsConfigService from '@/entities/files/sms-config/sms-config.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('SmsConfig Management Update Component', () => {
    let wrapper: Wrapper<SmsConfigClass>;
    let comp: SmsConfigClass;
    let smsConfigServiceStub: SinonStubbedInstance<SmsConfigService>;

    beforeEach(() => {
      smsConfigServiceStub = sinon.createStubInstance<SmsConfigService>(SmsConfigService);

      wrapper = shallowMount<SmsConfigClass>(SmsConfigUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          smsConfigService: () => smsConfigServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.smsConfig = entity;
        // @ts-ignore
        smsConfigServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(smsConfigServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.smsConfig = entity;
        // @ts-ignore
        smsConfigServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(smsConfigServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
