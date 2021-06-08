import DataDictionaryComponent from './data-dictionary.vue';
import DataDictionaryCompactComponent from './data-dictionary-compact.vue';
import DataDictionaryUpdate from './data-dictionary-update.vue';

const DataDictionary = {
  install: function (Vue) {
    Vue.component('jhi-data-dictionary', DataDictionaryComponent);
    Vue.component('jhi-data-dictionary-compact', DataDictionaryCompactComponent);
    Vue.component('jhi-data-dictionary-update', DataDictionaryUpdate);
  },
};

export default DataDictionary;
