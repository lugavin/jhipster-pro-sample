/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ViewPermissionDetailComponent from '@/entities/system/view-permission/view-permission-details.vue';
import ViewPermissionClass from '@/entities/system/view-permission/view-permission-details.component';
import ViewPermissionService from '@/entities/system/view-permission/view-permission.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ViewPermission Management Detail Component', () => {
    let wrapper: Wrapper<ViewPermissionClass>;
    let comp: ViewPermissionClass;
    let viewPermissionServiceStub: SinonStubbedInstance<ViewPermissionService>;

    beforeEach(() => {
      viewPermissionServiceStub = sinon.createStubInstance<ViewPermissionService>(ViewPermissionService);

      wrapper = shallowMount<ViewPermissionClass>(ViewPermissionDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { viewPermissionService: () => viewPermissionServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundViewPermission = { id: 123 };
        // @ts-ignore
        viewPermissionServiceStub.find.resolves(foundViewPermission);

        // WHEN
        comp.retrieveViewPermission(123);
        await comp.$nextTick();

        // THEN
        expect(comp.viewPermission).toBe(foundViewPermission);
      });
    });
  });
});
