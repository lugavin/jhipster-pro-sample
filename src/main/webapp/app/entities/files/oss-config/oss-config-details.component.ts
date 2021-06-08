import { Component, Vue, Inject } from 'vue-property-decorator';

import { IOssConfig } from '@/shared/model/files/oss-config.model';
import OssConfigService from './oss-config.service';

@Component
export default class OssConfigDetails extends Vue {
  @Inject('ossConfigService') private ossConfigService: () => OssConfigService;
  public ossConfig: IOssConfig = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ossConfigId) {
        vm.retrieveOssConfig(to.params.ossConfigId);
      }
    });
  }

  public retrieveOssConfig(ossConfigId) {
    this.ossConfigService()
      .find(ossConfigId)
      .subscribe(res => {
        this.ossConfig = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
