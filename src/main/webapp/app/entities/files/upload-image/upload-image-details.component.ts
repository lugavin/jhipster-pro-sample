import { Component, Vue, Inject } from 'vue-property-decorator';

import { IUploadImage } from '@/shared/model/files/upload-image.model';
import UploadImageService from './upload-image.service';

@Component
export default class UploadImageDetails extends Vue {
  @Inject('uploadImageService') private uploadImageService: () => UploadImageService;
  public uploadImage: IUploadImage = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.uploadImageId) {
        vm.retrieveUploadImage(to.params.uploadImageId);
      }
    });
  }

  public retrieveUploadImage(uploadImageId) {
    this.uploadImageService()
      .find(uploadImageId)
      .subscribe(res => {
        this.uploadImage = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
