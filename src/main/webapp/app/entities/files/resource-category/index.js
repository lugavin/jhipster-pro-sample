import ResourceCategoryComponent from './resource-category.vue';
import ResourceCategoryCompactComponent from './resource-category-compact.vue';
import ResourceCategoryUpdate from './resource-category-update.vue';

const ResourceCategory = {
  install: function (Vue) {
    Vue.component('jhi-resource-category', ResourceCategoryComponent);
    Vue.component('jhi-resource-category-compact', ResourceCategoryCompactComponent);
    Vue.component('jhi-resource-category-update', ResourceCategoryUpdate);
  },
};

export default ResourceCategory;
