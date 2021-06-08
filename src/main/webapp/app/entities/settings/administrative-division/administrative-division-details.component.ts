import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAdministrativeDivision } from '@/shared/model/settings/administrative-division.model';
import AdministrativeDivisionService from './administrative-division.service';

@Component
export default class AdministrativeDivisionDetails extends Vue {
  @Inject('administrativeDivisionService') private administrativeDivisionService: () => AdministrativeDivisionService;
  public administrativeDivision: IAdministrativeDivision = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.administrativeDivisionId) {
        vm.retrieveAdministrativeDivision(to.params.administrativeDivisionId);
      }
    });
  }

  public retrieveAdministrativeDivision(administrativeDivisionId) {
    this.administrativeDivisionService()
      .find(administrativeDivisionId)
      .subscribe(res => {
        this.administrativeDivision = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
