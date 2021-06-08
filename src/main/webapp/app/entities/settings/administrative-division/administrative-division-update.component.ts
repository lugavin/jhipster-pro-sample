import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import AlertService from '@/shared/alert/alert.service';
import { IAdministrativeDivision, AdministrativeDivision } from '@/shared/model/settings/administrative-division.model';
import AdministrativeDivisionService from './administrative-division.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  administrativeDivision: {
    id: {},
    name: {},
    areaCode: {},
    cityCode: {},
    mergerName: {},
    shortName: {},
    zipCode: {},
    level: {},
    lng: {},
    lat: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class AdministrativeDivisionUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('administrativeDivisionService') private administrativeDivisionService: () => AdministrativeDivisionService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public administrativeDivision: IAdministrativeDivision = new AdministrativeDivision();

  public administrativeDivisions: IAdministrativeDivision[] = [];
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
  public administrativeDivisionId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.administrativeDivisionId) {
      this.administrativeDivisionId = this.$route.params.administrativeDivisionId;
    }
    if (this.$route.params.parentId) {
      this.administrativeDivision.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.administrativeDivision.parent.id = this.parentId;
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
        Object.assign(that.administrativeDivision, values);
        if (that.administrativeDivision.id) {
          this.administrativeDivisionService()
            .update(that.administrativeDivision)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsAdministrativeDivision.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.administrativeDivisionService()
            .create(this.administrativeDivision)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsAdministrativeDivision.created', { param: param.data.id }).toString();
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

  public retrieveAdministrativeDivision(administrativeDivisionId): void {
    if (!administrativeDivisionId) {
      return;
    }
    this.administrativeDivisionService()
      .find(administrativeDivisionId)
      .subscribe(res => {
        this.administrativeDivision = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.administrativeDivisionService().tree()]).subscribe(
      ([administrativeDivisionsRes]) => {
        this.relationshipsData['administrativeDivisions'] = administrativeDivisionsRes.data;
        this.relationshipsData['entity'] = this.administrativeDivision;
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
    if (this.administrativeDivisionId || this.updateEntityId) {
      this.retrieveAdministrativeDivision(this.administrativeDivisionId || this.updateEntityId);
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
        .findByEntityName('AdministrativeDivision')
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
        if (item.type === 'modalSelect' && this.administrativeDivision[item.key]) {
          const isArray = Array.isArray(this.administrativeDivision[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.administrativeDivision[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.administrativeDivision[item.key]) {
                const findIds = this.administrativeDivision[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.administrativeDivision[item.key]) {
              if (
                !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.administrativeDivision[item.key])
              ) {
                options[propertyName] = this.administrativeDivision[item.key];
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
    this.relationshipsData['entity'] = this.administrativeDivision;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.administrativeDivision)); // loadsh的pick方法
    });
  }
}
