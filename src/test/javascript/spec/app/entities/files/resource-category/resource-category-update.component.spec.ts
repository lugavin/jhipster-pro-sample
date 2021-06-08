/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ResourceCategoryUpdateComponent from '@/entities/files/resource-category/resource-category-update.vue';
import ResourceCategoryClass from '@/entities/files/resource-category/resource-category-update.component';
import ResourceCategoryService from '@/entities/files/resource-category/resource-category.service';

import UploadFileService from '@/entities/files/upload-file/upload-file.service';

import UploadImageService from '@/entities/files/upload-image/upload-image.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('ResourceCategory Management Update Component', () => {
    let wrapper: Wrapper<ResourceCategoryClass>;
    let comp: ResourceCategoryClass;
    let resourceCategoryServiceStub: SinonStubbedInstance<ResourceCategoryService>;

    beforeEach(() => {
      resourceCategoryServiceStub = sinon.createStubInstance<ResourceCategoryService>(ResourceCategoryService);

      wrapper = shallowMount<ResourceCategoryClass>(ResourceCategoryUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          resourceCategoryService: () => resourceCategoryServiceStub,

          uploadFileService: () => new UploadFileService(),

          uploadImageService: () => new UploadImageService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.resourceCategory = entity;
        // @ts-ignore
        resourceCategoryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(resourceCategoryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.resourceCategory = entity;
        // @ts-ignore
        resourceCategoryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(resourceCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
