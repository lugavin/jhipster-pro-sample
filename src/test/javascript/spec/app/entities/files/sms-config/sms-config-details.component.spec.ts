/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SmsConfigDetailComponent from '@/entities/files/sms-config/sms-config-details.vue';
import SmsConfigClass from '@/entities/files/sms-config/sms-config-details.component';
import SmsConfigService from '@/entities/files/sms-config/sms-config.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('SmsConfig Management Detail Component', () => {
    let wrapper: Wrapper<SmsConfigClass>;
    let comp: SmsConfigClass;
    let smsConfigServiceStub: SinonStubbedInstance<SmsConfigService>;

    beforeEach(() => {
      smsConfigServiceStub = sinon.createStubInstance<SmsConfigService>(SmsConfigService);

      wrapper = shallowMount<SmsConfigClass>(SmsConfigDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { smsConfigService: () => smsConfigServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSmsConfig = { id: 123 };
        // @ts-ignore
        smsConfigServiceStub.find.resolves(foundSmsConfig);

        // WHEN
        comp.retrieveSmsConfig(123);
        await comp.$nextTick();

        // THEN
        expect(comp.smsConfig).toBe(foundSmsConfig);
      });
    });
  });
});
