import BusinessTypeComponent from './business-type.vue';
import BusinessTypeCompactComponent from './business-type-compact.vue';
import BusinessTypeUpdate from './business-type-update.vue';

const BusinessType = {
  install: function (Vue) {
    Vue.component('jhi-business-type', BusinessTypeComponent);
    Vue.component('jhi-business-type-compact', BusinessTypeCompactComponent);
    Vue.component('jhi-business-type-update', BusinessTypeUpdate);
  },
};

export default BusinessType;
