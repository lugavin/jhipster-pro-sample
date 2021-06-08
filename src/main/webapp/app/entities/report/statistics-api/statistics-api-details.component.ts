import { Component, Vue, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IStatisticsApi } from '@/shared/model/report/statistics-api.model';
import StatisticsApiService from './statistics-api.service';

@Component
export default class StatisticsApiDetails extends Vue {
  @Inject('statisticsApiService') private statisticsApiService: () => StatisticsApiService;
  public statisticsApi: IStatisticsApi = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.statisticsApiId) {
        vm.retrieveStatisticsApi(to.params.statisticsApiId);
      }
    });
  }

  public retrieveStatisticsApi(statisticsApiId) {
    this.statisticsApiService()
      .find(statisticsApiId)
      .subscribe(res => {
        this.statisticsApi = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
