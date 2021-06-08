import { Component, Vue, Inject } from 'vue-property-decorator';

import { IViewPermission } from '@/shared/model/system/view-permission.model';
import ViewPermissionService from './view-permission.service';

@Component
export default class ViewPermissionDetails extends Vue {
  @Inject('viewPermissionService') private viewPermissionService: () => ViewPermissionService;
  public viewPermission: IViewPermission = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.viewPermissionId) {
        vm.retrieveViewPermission(to.params.viewPermissionId);
      }
    });
  }

  public retrieveViewPermission(viewPermissionId) {
    this.viewPermissionService()
      .find(viewPermissionId)
      .subscribe(res => {
        this.viewPermission = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
