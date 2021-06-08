import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICommonTableRelationship } from '@/shared/model/modelConfig/common-table-relationship.model';
import CommonTableRelationshipService from './common-table-relationship.service';

@Component
export default class CommonTableRelationshipDetails extends Vue {
  @Inject('commonTableRelationshipService') private commonTableRelationshipService: () => CommonTableRelationshipService;
  public commonTableRelationship: ICommonTableRelationship = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commonTableRelationshipId) {
        vm.retrieveCommonTableRelationship(to.params.commonTableRelationshipId);
      }
    });
  }

  public retrieveCommonTableRelationship(commonTableRelationshipId) {
    this.commonTableRelationshipService()
      .find(commonTableRelationshipId)
      .subscribe(res => {
        this.commonTableRelationship = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
