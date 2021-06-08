import CommonConditionComponent from './common-condition.vue';
import CommonConditionCompactComponent from './common-condition-compact.vue';
import CommonConditionUpdate from './common-condition-update.vue';

const CommonCondition = {
  install: function (Vue) {
    Vue.component('jhi-common-condition', CommonConditionComponent);
    Vue.component('jhi-common-condition-compact', CommonConditionCompactComponent);
    Vue.component('jhi-common-condition-update', CommonConditionUpdate);
  },
};

export default CommonCondition;
