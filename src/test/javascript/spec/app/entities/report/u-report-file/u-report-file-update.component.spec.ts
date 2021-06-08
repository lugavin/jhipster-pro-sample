/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import UReportFileUpdateComponent from '@/entities/report/u-report-file/u-report-file-update.vue';
import UReportFileClass from '@/entities/report/u-report-file/u-report-file-update.component';
import UReportFileService from '@/entities/report/u-report-file/u-report-file.service';

import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('UReportFile Management Update Component', () => {
    let wrapper: Wrapper<UReportFileClass>;
    let comp: UReportFileClass;
    let uReportFileServiceStub: SinonStubbedInstance<UReportFileService>;

    beforeEach(() => {
      uReportFileServiceStub = sinon.createStubInstance<UReportFileService>(UReportFileService);

      wrapper = shallowMount<UReportFileClass>(UReportFileUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          uReportFileService: () => uReportFileServiceStub,

          commonTableService: () => new CommonTableService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.uReportFile = entity;
        // @ts-ignore
        uReportFileServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uReportFileServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.uReportFile = entity;
        // @ts-ignore
        uReportFileServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(uReportFileServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
