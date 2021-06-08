import UserComponent from './user.vue';
import UserCompactComponent from './user-compact.vue';

const User = {
  install: function (Vue) {
    Vue.component('jhi-user', UserComponent);
    Vue.component('jhi-user-compact', UserCompactComponent);
  },
};

export default User;
