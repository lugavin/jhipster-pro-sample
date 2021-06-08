import SmsConfigComponent from './sms-config.vue';
import SmsConfigCompactComponent from './sms-config-compact.vue';
import SmsConfigUpdate from './sms-config-update.vue';

const SmsConfig = {
  install: function (Vue) {
    Vue.component('jhi-sms-config', SmsConfigComponent);
    Vue.component('jhi-sms-config-compact', SmsConfigCompactComponent);
    Vue.component('jhi-sms-config-update', SmsConfigUpdate);
  },
};

export default SmsConfig;
