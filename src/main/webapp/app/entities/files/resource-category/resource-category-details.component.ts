import { Component, Vue, Inject } from 'vue-property-decorator';

import { IResourceCategory } from '@/shared/model/files/resource-category.model';
import ResourceCategoryService from './resource-category.service';

@Component
export default class ResourceCategoryDetails extends Vue {
  @Inject('resourceCategoryService') private resourceCategoryService: () => ResourceCategoryService;
  public resourceCategory: IResourceCategory = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.resourceCategoryId) {
        vm.retrieveResourceCategory(to.params.resourceCategoryId);
      }
    });
  }

  public retrieveResourceCategory(resourceCategoryId) {
    this.resourceCategoryService()
      .find(resourceCategoryId)
      .subscribe(res => {
        this.resourceCategory = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
