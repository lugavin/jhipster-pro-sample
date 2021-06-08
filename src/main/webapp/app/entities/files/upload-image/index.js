import UploadImageComponent from './upload-image.vue';
import UploadImageCompactComponent from './upload-image-compact.vue';
import UploadImageUpdate from './upload-image-update.vue';

const UploadImage = {
  install: function (Vue) {
    Vue.component('jhi-upload-image', UploadImageComponent);
    Vue.component('jhi-upload-image-compact', UploadImageCompactComponent);
    Vue.component('jhi-upload-image-update', UploadImageUpdate);
  },
};

export default UploadImage;
