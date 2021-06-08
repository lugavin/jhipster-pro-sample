import CommonTableRelationshipComponent from './common-table-relationship.vue';
import CommonTableRelationshipCompactComponent from './common-table-relationship-compact.vue';
import CommonTableRelationshipUpdate from './common-table-relationship-update.vue';

const CommonTableRelationship = {
  install: function (Vue) {
    Vue.component('jhi-common-table-relationship', CommonTableRelationshipComponent);
    Vue.component('jhi-common-table-relationship-compact', CommonTableRelationshipCompactComponent);
    Vue.component('jhi-common-table-relationship-update', CommonTableRelationshipUpdate);
  },
};

export default CommonTableRelationship;
