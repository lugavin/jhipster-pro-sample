import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICommonTableField } from '@/shared/model/modelConfig/common-table-field.model';
import CommonTableFieldService from './common-table-field.service';

@Component
export default class CommonTableFieldDetails extends Vue {
  @Inject('commonTableFieldService') private commonTableFieldService: () => CommonTableFieldService;
  public commonTableField: ICommonTableField = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commonTableFieldId) {
        vm.retrieveCommonTableField(to.params.commonTableFieldId);
      }
    });
  }

  public retrieveCommonTableField(commonTableFieldId) {
    this.commonTableFieldService()
      .find(commonTableFieldId)
      .subscribe(res => {
        this.commonTableField = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
