import { Component, Vue, Inject } from 'vue-property-decorator';

import { IUploadFile } from '@/shared/model/files/upload-file.model';
import UploadFileService from './upload-file.service';

@Component
export default class UploadFileDetails extends Vue {
  @Inject('uploadFileService') private uploadFileService: () => UploadFileService;
  public uploadFile: IUploadFile = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.uploadFileId) {
        vm.retrieveUploadFile(to.params.uploadFileId);
      }
    });
  }

  public retrieveUploadFile(uploadFileId) {
    this.uploadFileService()
      .find(uploadFileId)
      .subscribe(res => {
        this.uploadFile = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
