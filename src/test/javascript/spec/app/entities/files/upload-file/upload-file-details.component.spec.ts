/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import UploadFileDetailComponent from '@/entities/files/upload-file/upload-file-details.vue';
import UploadFileClass from '@/entities/files/upload-file/upload-file-details.component';
import UploadFileService from '@/entities/files/upload-file/upload-file.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('UploadFile Management Detail Component', () => {
    let wrapper: Wrapper<UploadFileClass>;
    let comp: UploadFileClass;
    let uploadFileServiceStub: SinonStubbedInstance<UploadFileService>;

    beforeEach(() => {
      uploadFileServiceStub = sinon.createStubInstance<UploadFileService>(UploadFileService);

      wrapper = shallowMount<UploadFileClass>(UploadFileDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { uploadFileService: () => uploadFileServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundUploadFile = { id: 123 };
        // @ts-ignore
        uploadFileServiceStub.find.resolves(foundUploadFile);

        // WHEN
        comp.retrieveUploadFile(123);
        await comp.$nextTick();

        // THEN
        expect(comp.uploadFile).toBe(foundUploadFile);
      });
    });
  });
});
