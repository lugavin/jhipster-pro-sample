/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import OssConfigUpdateComponent from '@/entities/files/oss-config/oss-config-update.vue';
import OssConfigClass from '@/entities/files/oss-config/oss-config-update.component';
import OssConfigService from '@/entities/files/oss-config/oss-config.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('OssConfig Management Update Component', () => {
    let wrapper: Wrapper<OssConfigClass>;
    let comp: OssConfigClass;
    let ossConfigServiceStub: SinonStubbedInstance<OssConfigService>;

    beforeEach(() => {
      ossConfigServiceStub = sinon.createStubInstance<OssConfigService>(OssConfigService);

      wrapper = shallowMount<OssConfigClass>(OssConfigUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          ossConfigService: () => ossConfigServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.ossConfig = entity;
        // @ts-ignore
        ossConfigServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ossConfigServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.ossConfig = entity;
        // @ts-ignore
        ossConfigServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ossConfigServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
