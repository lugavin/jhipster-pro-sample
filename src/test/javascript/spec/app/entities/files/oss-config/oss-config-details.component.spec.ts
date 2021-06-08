/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import OssConfigDetailComponent from '@/entities/files/oss-config/oss-config-details.vue';
import OssConfigClass from '@/entities/files/oss-config/oss-config-details.component';
import OssConfigService from '@/entities/files/oss-config/oss-config.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('OssConfig Management Detail Component', () => {
    let wrapper: Wrapper<OssConfigClass>;
    let comp: OssConfigClass;
    let ossConfigServiceStub: SinonStubbedInstance<OssConfigService>;

    beforeEach(() => {
      ossConfigServiceStub = sinon.createStubInstance<OssConfigService>(OssConfigService);

      wrapper = shallowMount<OssConfigClass>(OssConfigDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { ossConfigService: () => ossConfigServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundOssConfig = { id: 123 };
        // @ts-ignore
        ossConfigServiceStub.find.resolves(foundOssConfig);

        // WHEN
        comp.retrieveOssConfig(123);
        await comp.$nextTick();

        // THEN
        expect(comp.ossConfig).toBe(foundOssConfig);
      });
    });
  });
});
