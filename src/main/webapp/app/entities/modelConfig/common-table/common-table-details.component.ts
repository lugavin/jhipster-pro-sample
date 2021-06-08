import { Component, Vue, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import CommonTableService from './common-table.service';

@Component
export default class CommonTableDetails extends Vue {
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public commonTable: ICommonTable = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commonTableId) {
        vm.retrieveCommonTable(to.params.commonTableId);
      }
    });
  }

  public retrieveCommonTable(commonTableId) {
    this.commonTableService()
      .find(commonTableId)
      .subscribe(res => {
        this.commonTable = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
