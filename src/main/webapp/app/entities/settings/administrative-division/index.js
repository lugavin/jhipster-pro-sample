import AdministrativeDivisionComponent from './administrative-division.vue';
import AdministrativeDivisionCompactComponent from './administrative-division-compact.vue';
import AdministrativeDivisionUpdate from './administrative-division-update.vue';

const AdministrativeDivision = {
  install: function (Vue) {
    Vue.component('jhi-administrative-division', AdministrativeDivisionComponent);
    Vue.component('jhi-administrative-division-compact', AdministrativeDivisionCompactComponent);
    Vue.component('jhi-administrative-division-update', AdministrativeDivisionUpdate);
  },
};

export default AdministrativeDivision;
