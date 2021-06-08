import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAuthority } from '@/shared/model/system/authority.model';
import AuthorityService from './authority.service';

@Component
export default class AuthorityDetails extends Vue {
  @Inject('authorityService') private authorityService: () => AuthorityService;
  public authority: IAuthority = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.authorityId) {
        vm.retrieveAuthority(to.params.authorityId);
      }
    });
  }

  public retrieveAuthority(authorityId) {
    this.authorityService()
      .find(authorityId)
      .subscribe(res => {
        this.authority = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
