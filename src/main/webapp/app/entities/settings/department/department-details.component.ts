import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDepartment } from '@/shared/model/settings/department.model';
import DepartmentService from './department.service';

@Component
export default class DepartmentDetails extends Vue {
  @Inject('departmentService') private departmentService: () => DepartmentService;
  public department: IDepartment = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.departmentId) {
        vm.retrieveDepartment(to.params.departmentId);
      }
    });
  }

  public retrieveDepartment(departmentId) {
    this.departmentService()
      .find(departmentId)
      .subscribe(res => {
        this.department = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
