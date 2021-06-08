import { Component, Vue, Inject } from 'vue-property-decorator';

import { IApiPermission } from '@/shared/model/system/api-permission.model';
import ApiPermissionService from './api-permission.service';

@Component
export default class ApiPermissionDetails extends Vue {
  @Inject('apiPermissionService') private apiPermissionService: () => ApiPermissionService;
  public apiPermission: IApiPermission = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.apiPermissionId) {
        vm.retrieveApiPermission(to.params.apiPermissionId);
      }
    });
  }

  public retrieveApiPermission(apiPermissionId) {
    this.apiPermissionService()
      .find(apiPermissionId)
      .subscribe(res => {
        this.apiPermission = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
