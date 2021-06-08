/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import StatisticsApiDetailComponent from '@/entities/report/statistics-api/statistics-api-details.vue';
import StatisticsApiClass from '@/entities/report/statistics-api/statistics-api-details.component';
import StatisticsApiService from '@/entities/report/statistics-api/statistics-api.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('StatisticsApi Management Detail Component', () => {
    let wrapper: Wrapper<StatisticsApiClass>;
    let comp: StatisticsApiClass;
    let statisticsApiServiceStub: SinonStubbedInstance<StatisticsApiService>;

    beforeEach(() => {
      statisticsApiServiceStub = sinon.createStubInstance<StatisticsApiService>(StatisticsApiService);

      wrapper = shallowMount<StatisticsApiClass>(StatisticsApiDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { statisticsApiService: () => statisticsApiServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundStatisticsApi = { id: 123 };
        // @ts-ignore
        statisticsApiServiceStub.find.resolves(foundStatisticsApi);

        // WHEN
        comp.retrieveStatisticsApi(123);
        await comp.$nextTick();

        // THEN
        expect(comp.statisticsApi).toBe(foundStatisticsApi);
      });
    });
  });
});
