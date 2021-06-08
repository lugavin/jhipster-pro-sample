import Vue from 'vue';
import Vuex from 'vuex';

import app from './modules/app';
import user from './modules/user';
import { accountStore } from '@/shared/config/store/account-store';
import { alertStore } from '@/shared/config/store/alert-store';
import { translationStore } from '@/shared/config/store/translation-store';
import { processDesignStore } from '@/store/modules/process-design-store';

// default router permission control
import permission from './modules/permission';

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    user,
    permission,
    accountStore,
    alertStore,
    translationStore,
    processDesignStore,
  },
  state: {},
  mutations: {},
  actions: {},
  getters,
});
