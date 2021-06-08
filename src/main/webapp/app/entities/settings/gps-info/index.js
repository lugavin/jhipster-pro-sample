import GpsInfoComponent from './gps-info.vue';
import GpsInfoCompactComponent from './gps-info-compact.vue';
import GpsInfoUpdate from './gps-info-update.vue';

const GpsInfo = {
  install: function (Vue) {
    Vue.component('jhi-gps-info', GpsInfoComponent);
    Vue.component('jhi-gps-info-compact', GpsInfoCompactComponent);
    Vue.component('jhi-gps-info-update', GpsInfoUpdate);
  },
};

export default GpsInfo;
