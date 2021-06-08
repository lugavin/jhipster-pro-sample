import dataTableComponent from './DataTable.vue';

const dataTable = {
  install: function (Vue) {
    Vue.component('DataTable', dataTableComponent);
  },
};
export const DataTable = dataTableComponent;

export default dataTable;
