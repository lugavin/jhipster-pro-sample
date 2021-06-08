import SelectListModalCompent from './select-list-modal';
const SelectListModal = {
  install: function (Vue) {
    Vue.component('select-list-modal', SelectListModalCompent);
  },
};
// 导出组件
export default SelectListModal;
