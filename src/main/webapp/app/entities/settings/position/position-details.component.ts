import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPosition } from '@/shared/model/settings/position.model';
import PositionService from './position.service';

@Component
export default class PositionDetails extends Vue {
  @Inject('positionService') private positionService: () => PositionService;
  public position: IPosition = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.positionId) {
        vm.retrievePosition(to.params.positionId);
      }
    });
  }

  public retrievePosition(positionId) {
    this.positionService()
      .find(positionId)
      .subscribe(res => {
        this.position = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
