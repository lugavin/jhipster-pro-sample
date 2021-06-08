/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import UReportFileDetailComponent from '@/entities/report/u-report-file/u-report-file-details.vue';
import UReportFileClass from '@/entities/report/u-report-file/u-report-file-details.component';
import UReportFileService from '@/entities/report/u-report-file/u-report-file.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('UReportFile Management Detail Component', () => {
    let wrapper: Wrapper<UReportFileClass>;
    let comp: UReportFileClass;
    let uReportFileServiceStub: SinonStubbedInstance<UReportFileService>;

    beforeEach(() => {
      uReportFileServiceStub = sinon.createStubInstance<UReportFileService>(UReportFileService);

      wrapper = shallowMount<UReportFileClass>(UReportFileDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { uReportFileService: () => uReportFileServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundUReportFile = { id: 123 };
        // @ts-ignore
        uReportFileServiceStub.find.resolves(foundUReportFile);

        // WHEN
        comp.retrieveUReportFile(123);
        await comp.$nextTick();

        // THEN
        expect(comp.uReportFile).toBe(foundUReportFile);
      });
    });
  });
});
