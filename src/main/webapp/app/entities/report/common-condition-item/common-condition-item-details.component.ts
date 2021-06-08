import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICommonConditionItem } from '@/shared/model/report/common-condition-item.model';
import CommonConditionItemService from './common-condition-item.service';

@Component
export default class CommonConditionItemDetails extends Vue {
  @Inject('commonConditionItemService') private commonConditionItemService: () => CommonConditionItemService;
  public commonConditionItem: ICommonConditionItem = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commonConditionItemId) {
        vm.retrieveCommonConditionItem(to.params.commonConditionItemId);
      }
    });
  }

  public retrieveCommonConditionItem(commonConditionItemId) {
    this.commonConditionItemService()
      .find(commonConditionItemId)
      .subscribe(res => {
        this.commonConditionItem = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
