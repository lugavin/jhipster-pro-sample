import AuthorityComponent from './authority.vue';
import AuthorityCompactComponent from './authority-compact.vue';
import AuthorityUpdate from './authority-update.vue';

const Authority = {
  install: function (Vue) {
    Vue.component('jhi-authority', AuthorityComponent);
    Vue.component('jhi-authority-compact', AuthorityCompactComponent);
    Vue.component('jhi-authority-update', AuthorityUpdate);
  },
};

export default Authority;
