// with polyfills
import 'core-js/stable';
import 'regenerator-runtime/runtime';

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store/';
// import { VueAxios } from './utils/request';
import KFormDesign from '@/components/jhi-data-form';
// import dataTable from "@/components/dataTable";
// import 'k-form-design/lib/k-form-design.css';
import { UPLOAD_IMAGE_URL, UPLOAD_FILE_URL } from '@/constants';
const jwttoken = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
KFormDesign.setConfig({
  uploadFile: UPLOAD_FILE_URL, // 上传文件地址
  uploadImage: UPLOAD_IMAGE_URL, // 上传图片地址
  uploadFileName: 'file',
  uploadImageName: 'image',
  uploadFileHeaders: { Authorization: `Bearer ${jwttoken}` },
  uploadImageHeaders: { Authorization: `Bearer ${jwttoken}` },
});
import 'xe-utils';
import VXETable from 'vxe-table';
import 'vxe-table/lib/index.css';
import 'vxe-table-plugin-antd/dist/style.css';
Vue.use(VXETable);
import VXETablePluginAntd from 'vxe-table-plugin-antd';
import VXEAntdJhi from '@/components/vxe-select-list-modal';
VXETable.use(VXETablePluginAntd);
VXETable.use(VXEAntdJhi);
Vue.use(KFormDesign);
import SelectListModal from '@/components/select-list-modal';
Vue.use(SelectListModal);
import vcolorpicker from 'vcolorpicker';
Vue.use(vcolorpicker);
// mock
// WARNING: `mockjs` NOT SUPPORT `IE` PLEASE DO NOT USE IN `production` ENV.
// import './mock';
// import NProgress from 'nprogress'; // progress bar
// import '@/components/NProgress/nprogress.less'; // progress
import bootstrap from './core/bootstrap';
import './core/lazy_use';
// import './permission'; // permission control
import './utils/filter'; // global filter
import './components/global.less';
// @ts-ignore
Vue.component('v-chart', VueECharts);
import * as config from './shared/config/config';
import AlertService from '@/shared/alert/alert.service';
import AccountService from './account/account.service';
import TranslationService from '@/locale/translation.service';
import UserService from '@/shared/service/user.service';
import AuthorityService from '@/entities/system/authority/authority.service';
import Authority from '@/entities/system/authority';

Vue.use(Authority);
import CommonTableField from '@/entities/modelConfig/common-table-field';
Vue.use(CommonTableField);
import CommonTableFieldService from '@/entities/modelConfig/common-table-field/common-table-field.service';
import OssConfig from '@/entities/files/oss-config';
Vue.use(OssConfig);
import OssConfigService from '@/entities/files/oss-config/oss-config.service';
import ApiPermission from '@/entities/system/api-permission';
Vue.use(ApiPermission);
import ApiPermissionService from '@/entities/system/api-permission/api-permission.service';
import UploadImage from '@/entities/files/upload-image';
Vue.use(UploadImage);
import UploadImageService from '@/entities/files/upload-image/upload-image.service';
import SmsConfig from '@/entities/files/sms-config';
Vue.use(SmsConfig);
import SmsConfigService from '@/entities/files/sms-config/sms-config.service';
import UReportFile from '@/entities/report/u-report-file';
Vue.use(UReportFile);
import UReportFileService from '@/entities/report/u-report-file/u-report-file.service';
import Department from '@/entities/settings/department';
Vue.use(Department);
import DepartmentService from '@/entities/settings/department/department.service';
import StatisticsApi from '@/entities/report/statistics-api';
Vue.use(StatisticsApi);
import StatisticsApiService from '@/entities/report/statistics-api/statistics-api.service';
import BusinessType from '@/entities/settings/business-type';
Vue.use(BusinessType);
import BusinessTypeService from '@/entities/settings/business-type/business-type.service';
import CommonTableRelationship from '@/entities/modelConfig/common-table-relationship';
Vue.use(CommonTableRelationship);
import CommonTableRelationshipService from '@/entities/modelConfig/common-table-relationship/common-table-relationship.service';
import ViewPermission from '@/entities/system/view-permission';
Vue.use(ViewPermission);
import ViewPermissionService from '@/entities/system/view-permission/view-permission.service';
import CommonConditionItem from '@/entities/report/common-condition-item';
Vue.use(CommonConditionItem);
import CommonConditionItemService from '@/entities/report/common-condition-item/common-condition-item.service';
import AdministrativeDivision from '@/entities/settings/administrative-division';
Vue.use(AdministrativeDivision);
import AdministrativeDivisionService from '@/entities/settings/administrative-division/administrative-division.service';
import CommonTable from '@/entities/modelConfig/common-table';
Vue.use(CommonTable);
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import ResourceCategory from '@/entities/files/resource-category';
Vue.use(ResourceCategory);
import ResourceCategoryService from '@/entities/files/resource-category/resource-category.service';
import UploadFile from '@/entities/files/upload-file';
Vue.use(UploadFile);
import UploadFileService from '@/entities/files/upload-file/upload-file.service';
import Position from '@/entities/settings/position';
Vue.use(Position);
import PositionService from '@/entities/settings/position/position.service';
import DataDictionary from '@/entities/settings/data-dictionary';
Vue.use(DataDictionary);
import DataDictionaryService from '@/entities/settings/data-dictionary/data-dictionary.service';
import CommonCondition from '@/entities/report/common-condition';
Vue.use(CommonCondition);
import CommonConditionService from '@/entities/report/common-condition/common-condition.service';
import GpsInfo from '@/entities/settings/gps-info';
Vue.use(GpsInfo);
import GpsInfoService from '@/entities/settings/gps-info/gps-info.service';
// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

config.initVueApp(Vue);
const i18n = config.initI18N(Vue);
const translationService = new TranslationService(store, i18n);
const viewPermissionService = new ViewPermissionService();
const alertService = new AlertService(store);
const accountService = new AccountService(store, translationService, router, viewPermissionService);
import User from '@/entities/system/user';
Vue.use(User);
Vue.config.productionTip = false;

// mount axios Vue.$http and this.$http
// Vue.use(VueAxios);
router.beforeEach((to, from, next) => {
  // NProgress.start(); // start progress bar
  if (!to.matched.length) {
    next('/404');
    // NProgress.done(); // if current page is login will not trigger afterEach hook, so manually handle it
  }

  if (to.meta && to.meta.authorities && to.meta.authorities.length > 0) {
    if (!accountService.hasAnyAuthority(to.meta.authorities)) {
      sessionStorage.setItem('requested-url', to.fullPath);
      next('/user/login');
      // NProgress.done(); // if current page is login will not trigger afterEach hook, so manually handle it
    } else {
      // 将路由数据转换为菜单加入到系统中。
      next();
    }
  } else {
    // no authorities, so just proceed
    next();
  }
});

router.afterEach(() => {
  // NProgress.done(); // finish progress bar
});

new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  i18n,
  store,
  created: bootstrap,
  provide: {
    accountService: () => accountService,
    alertService: () => alertService,
    userService: () => new UserService(),
    authorityService: () => new AuthorityService(),
    commonTableFieldService: () => new CommonTableFieldService(),
    ossConfigService: () => new OssConfigService(),
    apiPermissionService: () => new ApiPermissionService(),
    uploadImageService: () => new UploadImageService(),
    smsConfigService: () => new SmsConfigService(),
    uReportFileService: () => new UReportFileService(),
    departmentService: () => new DepartmentService(),
    statisticsApiService: () => new StatisticsApiService(),
    businessTypeService: () => new BusinessTypeService(),
    commonTableRelationshipService: () => new CommonTableRelationshipService(),
    viewPermissionService: () => new ViewPermissionService(),
    commonConditionItemService: () => new CommonConditionItemService(),
    administrativeDivisionService: () => new AdministrativeDivisionService(),
    commonTableService: () => new CommonTableService(),
    resourceCategoryService: () => new ResourceCategoryService(),
    uploadFileService: () => new UploadFileService(),
    positionService: () => new PositionService(),
    dataDictionaryService: () => new DataDictionaryService(),
    commonConditionService: () => new CommonConditionService(),
    gpsInfoService: () => new GpsInfoService(),
    // jhipster-needle-add-entity-service-to-main - JHipster will import entities services here

    translationService: () => translationService,
  },
});
