/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import UploadFileUpdateComponent from '@/entities/files/upload-file/upload-file-update.vue';
import UploadFileClass from '@/entities/files/upload-file/upload-file-update.component';
import UploadFileService from '@/entities/files/upload-file/upload-file.service';

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
  describe('UploadFile Management Update Component', () => {
    let wrapper: Wrapper<UploadFileClass>;
    let comp: UploadFileClass;
    let uploadFileServiceStub: SinonStubbedInstance<UploadFileService>;

    beforeEach(() => {
      uploadFileServiceStub = sinon.createStubInstance<UploadFileService>(UploadFileService);

      wrapper = shallowMount<UploadFileClass>(UploadFileUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          uploadFileService: () => uploadFileServiceStub,

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
        comp.uploadFile = entity;
        // @ts-ignore
        uploadFileServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uploadFileServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.uploadFile = entity;
        // @ts-ignore
        uploadFileServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uploadFileServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
