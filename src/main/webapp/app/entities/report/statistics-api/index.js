import StatisticsApiComponent from './statistics-api.vue';
import StatisticsApiCompactComponent from './statistics-api-compact.vue';
import StatisticsApiUpdate from './statistics-api-update.vue';

const StatisticsApi = {
  install: function (Vue) {
    Vue.component('jhi-statistics-api', StatisticsApiComponent);
    Vue.component('jhi-statistics-api-compact', StatisticsApiCompactComponent);
    Vue.component('jhi-statistics-api-update', StatisticsApiUpdate);
  },
};

export default StatisticsApi;
