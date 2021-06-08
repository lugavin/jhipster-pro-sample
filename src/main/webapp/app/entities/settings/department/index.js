import DepartmentComponent from './department.vue';
import DepartmentCompactComponent from './department-compact.vue';
import DepartmentUpdate from './department-update.vue';

const Department = {
  install: function (Vue) {
    Vue.component('jhi-department', DepartmentComponent);
    Vue.component('jhi-department-compact', DepartmentCompactComponent);
    Vue.component('jhi-department-update', DepartmentUpdate);
  },
};

export default Department;
