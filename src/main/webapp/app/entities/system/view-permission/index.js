import ViewPermissionComponent from './view-permission.vue';
import ViewPermissionCompactComponent from './view-permission-compact.vue';
import ViewPermissionUpdate from './view-permission-update.vue';

const ViewPermission = {
  install: function (Vue) {
    Vue.component('jhi-view-permission', ViewPermissionComponent);
    Vue.component('jhi-view-permission-compact', ViewPermissionCompactComponent);
    Vue.component('jhi-view-permission-update', ViewPermissionUpdate);
  },
};

export default ViewPermission;
