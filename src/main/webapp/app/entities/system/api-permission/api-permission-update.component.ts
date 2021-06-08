import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import AuthorityService from '../../system/authority/authority.service';
import { IAuthority } from '@/shared/model/system/authority.model';

import AlertService from '@/shared/alert/alert.service';
import { IApiPermission, ApiPermission } from '@/shared/model/system/api-permission.model';
import ApiPermissionService from './api-permission.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  apiPermission: {
    id: {},
    serviceName: {},
    name: {},
    code: {},
    description: {},
    type: {},
    method: {},
    url: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class ApiPermissionUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('apiPermissionService') private apiPermissionService: () => ApiPermissionService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public apiPermission: IApiPermission = new ApiPermission();

  public apiPermissions: IApiPermission[] = [];

  @Inject('authorityService') private authorityService: () => AuthorityService;

  public authorities: IAuthority[] = [];
  public isSaving = false;
  public loading = false;
  @Ref('updateForm') readonly updateForm: any;
  public formJsonData = {
    list: [],
    config: {
      layout: 'horizontal',
      labelCol: { span: 4 },
      wrapperCol: { span: 18 },
      hideRequiredMark: false,
      customStyle: '',
    },
  };
  public relationshipsData: any = {};
  public dataFormContent = [];
  @Prop(Number) updateEntityId;
  @Prop(Number) parentId;
  @Prop(Boolean) showInModal;
  public dataContent = [];
  public apiPermissionId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.apiPermissionId) {
      this.apiPermissionId = this.$route.params.apiPermissionId;
    }
    if (this.$route.params.parentId) {
      this.apiPermission.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.apiPermission.parent.id = this.parentId;
    }
    this.initRelationships();
  }

  public mounted(): void {}

  public save(): void {
    this.isSaving = true;
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(that.apiPermission, values);
        if (that.apiPermission.id) {
          this.apiPermissionService()
            .update(that.apiPermission)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.systemApiPermission.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.apiPermissionService()
            .create(this.apiPermission)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.systemApiPermission.created', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'success');
              this.$router.go(-1);
            });
        }
      })
      .catch(err => {
        this.$message.error('数据获取失败！');
        console.log(err);
      });
  }

  public retrieveApiPermission(apiPermissionId): void {
    if (!apiPermissionId) {
      return;
    }
    this.apiPermissionService()
      .find(apiPermissionId)
      .subscribe(res => {
        this.apiPermission = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.apiPermissionService().tree(), this.authorityService().retrieve()]).subscribe(
      ([apiPermissionsRes, authoritiesRes]) => {
        this.relationshipsData['apiPermissions'] = apiPermissionsRes.data;
        this.relationshipsData['authorities'] = authoritiesRes.data;
        this.relationshipsData['entity'] = this.apiPermission;
        this.getData();
      },
      error => {
        this.loading = false;
        this.$message.error({
          content: `数据获取失败`,
          onClose: () => {
            this.getData();
          },
        });
      }
    );
  }
  public getData() {
    if (this.apiPermissionId || this.updateEntityId) {
      this.retrieveApiPermission(this.apiPermissionId || this.updateEntityId);
    } else {
      this.getFormData();
    }
  }
  public getFormData(formDataId?: number) {
    if (formDataId) {
      this.commonTableService()
        .find(formDataId)
        .subscribe(res => {
          this.updateFormData(res);
        });
    } else {
      this.commonTableService()
        .findByEntityName('ApiPermission')
        .subscribe(res => {
          this.updateFormData(res);
        });
    }
  }
  private updateFormData(res: any) {
    const commonTableData = res.data;
    if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
      this.formJsonData = JSON.parse(commonTableData.formConfig);
    } else {
      this.formJsonData.list = generateDataForDesigner(commonTableData);
    }
    if (this.formJsonData.list) {
      this.formJsonData.list.forEach(item => {
        if (item.type === 'modalSelect' && this.apiPermission[item.key]) {
          const isArray = Array.isArray(this.apiPermission[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.apiPermission[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.apiPermission[item.key]) {
                const findIds = this.apiPermission[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.apiPermission[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.apiPermission[item.key])) {
                options[propertyName] = this.apiPermission[item.key];
              }
            }
          }
          if (options[propertyName]) {
            this[lowerFirst(item.commonTableName + 'Service')]
              .call(this)
              .retrieve(options)
              .subscribe(res => {
                const newData = this.relationshipsData[item.options.dynamicKey].concat(res.data);
                delete this.relationshipsData[item.options.dynamicKey];
                this.$set(this.relationshipsData, item.options.dynamicKey, newData);
              });
          }
        }
      });
    }
    this.relationshipsData['entity'] = this.apiPermission;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.apiPermission)); // loadsh的pick方法
    });
  }
}
