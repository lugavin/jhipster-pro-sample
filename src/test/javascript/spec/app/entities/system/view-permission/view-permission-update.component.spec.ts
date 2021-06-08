/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ViewPermissionUpdateComponent from '@/entities/system/view-permission/view-permission-update.vue';
import ViewPermissionClass from '@/entities/system/view-permission/view-permission-update.component';
import ViewPermissionService from '@/entities/system/view-permission/view-permission.service';

import AuthorityService from '@/entities/system/authority/authority.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('ViewPermission Management Update Component', () => {
    let wrapper: Wrapper<ViewPermissionClass>;
    let comp: ViewPermissionClass;
    let viewPermissionServiceStub: SinonStubbedInstance<ViewPermissionService>;

    beforeEach(() => {
      viewPermissionServiceStub = sinon.createStubInstance<ViewPermissionService>(ViewPermissionService);

      wrapper = shallowMount<ViewPermissionClass>(ViewPermissionUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          viewPermissionService: () => viewPermissionServiceStub,

          authorityService: () => new AuthorityService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.viewPermission = entity;
        // @ts-ignore
        viewPermissionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(viewPermissionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.viewPermission = entity;
        // @ts-ignore
        viewPermissionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(viewPermissionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
