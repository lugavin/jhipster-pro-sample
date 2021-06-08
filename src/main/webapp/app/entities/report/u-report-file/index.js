import UReportFileComponent from './u-report-file.vue';
import UReportFileCompactComponent from './u-report-file-compact.vue';
import UReportFileUpdate from './u-report-file-update.vue';

const UReportFile = {
  install: function (Vue) {
    Vue.component('jhi-u-report-file', UReportFileComponent);
    Vue.component('jhi-u-report-file-compact', UReportFileCompactComponent);
    Vue.component('jhi-u-report-file-update', UReportFileUpdate);
  },
};

export default UReportFile;
