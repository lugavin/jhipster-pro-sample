import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISmsConfig } from '@/shared/model/files/sms-config.model';
import SmsConfigService from './sms-config.service';

@Component
export default class SmsConfigDetails extends Vue {
  @Inject('smsConfigService') private smsConfigService: () => SmsConfigService;
  public smsConfig: ISmsConfig = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.smsConfigId) {
        vm.retrieveSmsConfig(to.params.smsConfigId);
      }
    });
  }

  public retrieveSmsConfig(smsConfigId) {
    this.smsConfigService()
      .find(smsConfigId)
      .subscribe(res => {
        this.smsConfig = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
