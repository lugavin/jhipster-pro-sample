import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import moment from 'moment';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import AuthorityService from '../../system/authority/authority.service';
import { IAuthority } from '@/shared/model/system/authority.model';

import UserService from '@/shared/service/user.service';

import AlertService from '@/shared/alert/alert.service';
import { IDepartment, Department } from '@/shared/model/settings/department.model';
import DepartmentService from './department.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  department: {
    id: {},
    name: {},
    code: {},
    address: {},
    phoneNum: {},
    logo: {},
    contact: {},
    createUserId: {},
    createTime: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class DepartmentUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('departmentService') private departmentService: () => DepartmentService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public department: IDepartment = new Department();

  public departments: IDepartment[] = [];

  @Inject('authorityService') private authorityService: () => AuthorityService;

  public authorities: IAuthority[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
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
  public departmentId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.departmentId) {
      this.departmentId = this.$route.params.departmentId;
    }
    if (this.$route.params.parentId) {
      this.department.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.department.parent.id = this.parentId;
    }
    this.initRelationships();
    this.department.authorities = [];
  }

  public mounted(): void {}

  public save(): void {
    this.isSaving = true;
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(that.department, values);
        that.department.authorities = idsToIdObjectArray(this.department.authorities);
        if (that.department.id) {
          this.departmentService()
            .update(that.department)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsDepartment.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.departmentService()
            .create(this.department)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsDepartment.created', { param: param.data.id }).toString();
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

  public retrieveDepartment(departmentId): void {
    if (!departmentId) {
      return;
    }
    this.departmentService()
      .find(departmentId)
      .subscribe(res => {
        this.department = res.data;
        this.department.authorities = idObjectArrayToIdArray(this.department.authorities);
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.authorityService().retrieve(), this.departmentService().tree()]).subscribe(
      ([authoritiesRes, departmentsRes]) => {
        this.relationshipsData['authorities'] = authoritiesRes.data;
        this.relationshipsData['departments'] = departmentsRes.data;
        this.relationshipsData['entity'] = this.department;
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
    if (this.departmentId || this.updateEntityId) {
      this.retrieveDepartment(this.departmentId || this.updateEntityId);
    } else {
      this.getFormData();
    }
  }
  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
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
        .findByEntityName('Department')
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
        if (item.type === 'modalSelect' && this.department[item.key]) {
          const isArray = Array.isArray(this.department[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.department[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.department[item.key]) {
                const findIds = this.department[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.department[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.department[item.key])) {
                options[propertyName] = this.department[item.key];
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
    this.relationshipsData['entity'] = this.department;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.department)); // loadsh的pick方法
    });
  }
}
