import { Module } from 'vuex';

export const processDesignStore: Module<any, any> = {
  state: {
    commonTableId: null,
    processDefinitionKey: null,
    processDefinitionName: null,
  },
  getters: {
    commonTableId: state => state.commonTableId,
    processDefinitionKey: state => state.processDefinitionKey,
    processDefinitionName: state => state.processDefinitionName,
  },
  mutations: {
    initProcessData(state, processData) {
      state.commonTableId = processData.commonTableId;
      state.processDefinitionKey = processData.processDefinitionKey;
      state.processDefinitionName = processData.processDefinitionName;
    },
  },
};
