/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

// import format from 'date-fns/format';
// import parseISO from 'date-fns/parseISO';
// import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import StatisticsApiUpdateComponent from '@/entities/report/statistics-api/statistics-api-update.vue';
import StatisticsApiClass from '@/entities/report/statistics-api/statistics-api-update.component';
import StatisticsApiService from '@/entities/report/statistics-api/statistics-api.service';

import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';

import UserService from '@/admin/user-management/user-management.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('StatisticsApi Management Update Component', () => {
    let wrapper: Wrapper<StatisticsApiClass>;
    let comp: StatisticsApiClass;
    let statisticsApiServiceStub: SinonStubbedInstance<StatisticsApiService>;

    beforeEach(() => {
      statisticsApiServiceStub = sinon.createStubInstance<StatisticsApiService>(StatisticsApiService);

      wrapper = shallowMount<StatisticsApiClass>(StatisticsApiUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          statisticsApiService: () => statisticsApiServiceStub,

          commonTableService: () => new CommonTableService(),

          userService: () => new UserService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.statisticsApi = entity;
        // @ts-ignore
        statisticsApiServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(statisticsApiServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.statisticsApi = entity;
        // @ts-ignore
        statisticsApiServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(statisticsApiServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
