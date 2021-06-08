import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICommonCondition } from '@/shared/model/report/common-condition.model';
import CommonConditionService from './common-condition.service';

@Component
export default class CommonConditionDetails extends Vue {
  @Inject('commonConditionService') private commonConditionService: () => CommonConditionService;
  public commonCondition: ICommonCondition = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commonConditionId) {
        vm.retrieveCommonCondition(to.params.commonConditionId);
      }
    });
  }

  public retrieveCommonCondition(commonConditionId) {
    this.commonConditionService()
      .find(commonConditionId)
      .subscribe(res => {
        this.commonCondition = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
