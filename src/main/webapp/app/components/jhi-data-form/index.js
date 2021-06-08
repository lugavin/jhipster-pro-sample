/*
 * @Description: 组件输出文件
 * @Author: kcz
 * @Date: 2020-01-02 22:41:48
 * @LastEditors: kcz
 * @LastEditTime: 2020-04-12 22:51:46
 */
// 引入@babel/polyfill处理兼容
import 'core-js/stable';
import 'regenerator-runtime/runtime';
// 解决Chrome控制台non-passive event listener输出问题
import 'default-passive-events';
// 导入ant组件
// import "./core/components_use";
// import "ant-design-vue/dist/antd.less";

// 导入样式
import './styles/form-design.less';

// 导出本地iconfont
import './static/icons/iconfont';

// 导入单个组件
// import KChangeOption from "./KChangeOption/index.vue";
// import KCheckbox from "./KCheckbox/index.vue";
import KFormDesign from './KFormDesign/index';
import KFormPreview from './KFormPreview/index';
import KFormBuild from './KFormBuild/index';
import KFormItem from './KFormItem/index';
import { customComponents, basicsList } from './KFormDesign/config/formItemsConfig';
const components = [KFormDesign, KFormBuild, KFormItem, KFormPreview];

const install = function (Vue) {
  // use ant组件
  if (install.installed) return;
  install.installed = true;

  components.map(component => {
    Vue.component(component.name, component);
  });
};

if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue);
}

// import { Alert } from "ant-design-vue";

// // const UploadImg = () => import("../../UploadImg");
// // 自定义组件
// const customComponents = {
//   title: "自定义组件",
//   list: [
//     {
//       name: "测试",
//       type: "jkjksdf",
//       component: Alert,
//       options: {
//         multiple: false,
//         disabled: false,
//         width: "100%",
//         data: "{}",
//         limit: 3,
//         placeholder: "上传",
//         action: "",
//         listType: "picture-card"
//       },
//       model: "",
//       key: "",
//       rules: [
//         {
//           required: false,
//           message: "必填项"
//         }
//       ]
//     }
//   ]
// };

// /**
//  * @Author: kcz
//  * @description:
//  * @param {type}
//  * @return:
//  */
// export function getCustomComponents(){
//   // 获取自定义组件数据
//   return customComponents
// }

/**
 * @Author: kcz
 * @description: 配置组件及添加自定义组件
 * @return: Boolean
 * @param config
 */
function setFormDesignConfig(config) {
  try {
    customComponents.title = config.title || '自义定组件';
    customComponents.list = config.list || [];
    window.$customComponentList = config.list || [];
    const fileOptions = {
      action: config.uploadFile || '/', // 配置默认上传地址
      data: JSON.stringify(config.uploadFileData || {}), // 配置默认额外参数
      fileName: config.uploadFileName || 'file', // 配置默认name
      headers: config.uploadFileHeaders || {}, // 配置默认headers
    };
    const uploadFile = basicsList.find(item => item.type === 'uploadFile');
    Object.assign(uploadFile.options, fileOptions);
    const singleUploadFile = basicsList.find(item => item.type === 'singleUploadFile');
    Object.assign(singleUploadFile.options, fileOptions);

    const imageOptions = {
      action: config.uploadImage || '/', // 配置默认上传地址
      data: JSON.stringify(config.uploadImageData || {}), // 配置默认额外参数
      fileName: config.uploadImageName || 'file', // 配置默认name
      headers: config.uploadImageHeaders || {}, // 配置默认headers
    };
    const uploadImg = basicsList.find(item => item.type === 'uploadImg');
    Object.assign(uploadImg.options, imageOptions);
    const singleUploadImg = basicsList.find(item => item.type === 'singleUploadImg');
    Object.assign(singleUploadImg.options, imageOptions);
    return true;
  } catch {
    return false;
  }
}

// 这里可以用es6的解构语法导入组件 大概就是这个意思 毕竟没有用插件
export { KFormDesign, KFormBuild, KFormItem, KFormPreview, setFormDesignConfig };
export default {
  install,
  setConfig: setFormDesignConfig,
};
