import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGpsInfo } from '@/shared/model/settings/gps-info.model';
import GpsInfoService from './gps-info.service';

@Component
export default class GpsInfoDetails extends Vue {
  @Inject('gpsInfoService') private gpsInfoService: () => GpsInfoService;
  public gpsInfo: IGpsInfo = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.gpsInfoId) {
        vm.retrieveGpsInfo(to.params.gpsInfoId);
      }
    });
  }

  public retrieveGpsInfo(gpsInfoId) {
    this.gpsInfoService()
      .find(gpsInfoId)
      .subscribe(res => {
        this.gpsInfo = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
