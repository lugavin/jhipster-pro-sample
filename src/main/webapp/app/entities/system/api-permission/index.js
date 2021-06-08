import ApiPermissionComponent from './api-permission.vue';
import ApiPermissionCompactComponent from './api-permission-compact.vue';
import ApiPermissionUpdate from './api-permission-update.vue';

const ApiPermission = {
  install: function (Vue) {
    Vue.component('jhi-api-permission', ApiPermissionComponent);
    Vue.component('jhi-api-permission-compact', ApiPermissionCompactComponent);
    Vue.component('jhi-api-permission-update', ApiPermissionUpdate);
  },
};

export default ApiPermission;
