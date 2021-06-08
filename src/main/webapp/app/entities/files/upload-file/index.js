import UploadFileComponent from './upload-file.vue';
import UploadFileCompactComponent from './upload-file-compact.vue';
import UploadFileUpdate from './upload-file-update.vue';

const UploadFile = {
  install: function (Vue) {
    Vue.component('jhi-upload-file', UploadFileComponent);
    Vue.component('jhi-upload-file-compact', UploadFileCompactComponent);
    Vue.component('jhi-upload-file-update', UploadFileUpdate);
  },
};

export default UploadFile;
