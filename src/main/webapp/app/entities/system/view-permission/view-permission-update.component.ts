import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import AuthorityService from '../../system/authority/authority.service';
import { IAuthority } from '@/shared/model/system/authority.model';

import AlertService from '@/shared/alert/alert.service';
import { IViewPermission, ViewPermission } from '@/shared/model/system/view-permission.model';
import ViewPermissionService from './view-permission.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  viewPermission: {
    id: {},
    text: {},
    i18n: {},
    group: {},
    link: {},
    externalLink: {},
    target: {},
    icon: {},
    disabled: {},
    hide: {},
    hideInBreadcrumb: {},
    shortcut: {},
    shortcutRoot: {},
    reuse: {},
    code: {},
    description: {},
    type: {},
    order: {},
    apiPermissionCodes: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class ViewPermissionUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('viewPermissionService') private viewPermissionService: () => ViewPermissionService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public viewPermission: IViewPermission = new ViewPermission();

  public viewPermissions: IViewPermission[] = [];

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
  public viewPermissionId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.viewPermissionId) {
      this.viewPermissionId = this.$route.params.viewPermissionId;
    }
    if (this.$route.params.parentId) {
      this.viewPermission.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.viewPermission.parent.id = this.parentId;
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
        Object.assign(that.viewPermission, values);
        if (that.viewPermission.id) {
          this.viewPermissionService()
            .update(that.viewPermission)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.systemViewPermission.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.viewPermissionService()
            .create(this.viewPermission)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.systemViewPermission.created', { param: param.data.id }).toString();
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

  public retrieveViewPermission(viewPermissionId): void {
    if (!viewPermissionId) {
      return;
    }
    this.viewPermissionService()
      .find(viewPermissionId)
      .subscribe(res => {
        this.viewPermission = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.viewPermissionService().tree(), this.authorityService().retrieve()]).subscribe(
      ([viewPermissionsRes, authoritiesRes]) => {
        this.relationshipsData['viewPermissions'] = viewPermissionsRes.data;
        this.relationshipsData['authorities'] = authoritiesRes.data;
        this.relationshipsData['entity'] = this.viewPermission;
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
    if (this.viewPermissionId || this.updateEntityId) {
      this.retrieveViewPermission(this.viewPermissionId || this.updateEntityId);
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
        .findByEntityName('ViewPermission')
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
        if (item.type === 'modalSelect' && this.viewPermission[item.key]) {
          const isArray = Array.isArray(this.viewPermission[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.viewPermission[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.viewPermission[item.key]) {
                const findIds = this.viewPermission[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.viewPermission[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.viewPermission[item.key])) {
                options[propertyName] = this.viewPermission[item.key];
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
    this.relationshipsData['entity'] = this.viewPermission;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.viewPermission)); // loadsh的pick方法
    });
  }
}
