/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ApiPermissionDetailComponent from '@/entities/system/api-permission/api-permission-details.vue';
import ApiPermissionClass from '@/entities/system/api-permission/api-permission-details.component';
import ApiPermissionService from '@/entities/system/api-permission/api-permission.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ApiPermission Management Detail Component', () => {
    let wrapper: Wrapper<ApiPermissionClass>;
    let comp: ApiPermissionClass;
    let apiPermissionServiceStub: SinonStubbedInstance<ApiPermissionService>;

    beforeEach(() => {
      apiPermissionServiceStub = sinon.createStubInstance<ApiPermissionService>(ApiPermissionService);

      wrapper = shallowMount<ApiPermissionClass>(ApiPermissionDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { apiPermissionService: () => apiPermissionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundApiPermission = { id: 123 };
        // @ts-ignore
        apiPermissionServiceStub.find.resolves(foundApiPermission);

        // WHEN
        comp.retrieveApiPermission(123);
        await comp.$nextTick();

        // THEN
        expect(comp.apiPermission).toBe(foundApiPermission);
      });
    });
  });
});
