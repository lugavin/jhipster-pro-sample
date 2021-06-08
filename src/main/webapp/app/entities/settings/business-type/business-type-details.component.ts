import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBusinessType } from '@/shared/model/settings/business-type.model';
import BusinessTypeService from './business-type.service';

@Component
export default class BusinessTypeDetails extends Vue {
  @Inject('businessTypeService') private businessTypeService: () => BusinessTypeService;
  public businessType: IBusinessType = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.businessTypeId) {
        vm.retrieveBusinessType(to.params.businessTypeId);
      }
    });
  }

  public retrieveBusinessType(businessTypeId) {
    this.businessTypeService()
      .find(businessTypeId)
      .subscribe(res => {
        this.businessType = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
