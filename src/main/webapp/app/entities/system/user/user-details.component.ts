import { Component, Vue, Inject } from 'vue-property-decorator';

import UserService from '@/shared/service/user.service';
import { IUser } from '@/shared/model/user.model';

@Component
export default class PersonDetails extends Vue {
  @Inject('userService') private userService: () => UserService;
  public user: IUser = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.personId) {
        vm.retrievePerson(to.params.personId);
      }
    });
  }

  public retrievePerson(userId) {
    this.userService()
      .get(userId)
      .subscribe(res => {
        this.user = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
