/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import UploadImageUpdateComponent from '@/entities/files/upload-image/upload-image-update.vue';
import UploadImageClass from '@/entities/files/upload-image/upload-image-update.component';
import UploadImageService from '@/entities/files/upload-image/upload-image.service';

import UserService from '@/admin/user-management/user-management.service';

import ResourceCategoryService from '@/entities/files/resource-category/resource-category.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('UploadImage Management Update Component', () => {
    let wrapper: Wrapper<UploadImageClass>;
    let comp: UploadImageClass;
    let uploadImageServiceStub: SinonStubbedInstance<UploadImageService>;

    beforeEach(() => {
      uploadImageServiceStub = sinon.createStubInstance<UploadImageService>(UploadImageService);

      wrapper = shallowMount<UploadImageClass>(UploadImageUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          uploadImageService: () => uploadImageServiceStub,

          userService: () => new UserService(),

          resourceCategoryService: () => new ResourceCategoryService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.uploadImage = entity;
        // @ts-ignore
        uploadImageServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uploadImageServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.uploadImage = entity;
        // @ts-ignore
        uploadImageServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uploadImageServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
