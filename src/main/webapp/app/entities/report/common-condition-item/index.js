import CommonConditionItemComponent from './common-condition-item.vue';
import CommonConditionItemCompactComponent from './common-condition-item-compact.vue';
import CommonConditionItemUpdate from './common-condition-item-update.vue';

const CommonConditionItem = {
  install: function (Vue) {
    Vue.component('jhi-common-condition-item', CommonConditionItemComponent);
    Vue.component('jhi-common-condition-item-compact', CommonConditionItemCompactComponent);
    Vue.component('jhi-common-condition-item-update', CommonConditionItemUpdate);
  },
};

export default CommonConditionItem;
