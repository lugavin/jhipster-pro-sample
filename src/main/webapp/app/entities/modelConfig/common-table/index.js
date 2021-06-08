import CommonTableComponent from './common-table.vue';
import CommonTableCompactComponent from './common-table-compact.vue';
import CommonTableUpdate from './common-table-update.vue';

const CommonTable = {
  install: function (Vue) {
    Vue.component('jhi-common-table', CommonTableComponent);
    Vue.component('jhi-common-table-compact', CommonTableCompactComponent);
    Vue.component('jhi-common-table-update', CommonTableUpdate);
  },
};

export default CommonTable;
