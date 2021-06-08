import CommonTableFieldComponent from './common-table-field.vue';
import CommonTableFieldCompactComponent from './common-table-field-compact.vue';
import CommonTableFieldUpdate from './common-table-field-update.vue';

const CommonTableField = {
  install: function (Vue) {
    Vue.component('jhi-common-table-field', CommonTableFieldComponent);
    Vue.component('jhi-common-table-field-compact', CommonTableFieldCompactComponent);
    Vue.component('jhi-common-table-field-update', CommonTableFieldUpdate);
  },
};

export default CommonTableField;
