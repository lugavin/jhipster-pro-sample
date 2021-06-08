import OssConfigComponent from './oss-config.vue';
import OssConfigCompactComponent from './oss-config-compact.vue';
import OssConfigUpdate from './oss-config-update.vue';

const OssConfig = {
  install: function (Vue) {
    Vue.component('jhi-oss-config', OssConfigComponent);
    Vue.component('jhi-oss-config-compact', OssConfigCompactComponent);
    Vue.component('jhi-oss-config-update', OssConfigUpdate);
  },
};

export default OssConfig;
