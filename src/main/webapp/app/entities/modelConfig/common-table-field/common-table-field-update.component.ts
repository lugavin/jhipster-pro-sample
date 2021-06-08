import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import CommonTableService from '../../modelConfig/common-table/common-table.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';

import AlertService from '@/shared/alert/alert.service';
import { ICommonTableField, CommonTableField } from '@/shared/model/modelConfig/common-table-field.model';
import CommonTableFieldService from './common-table-field.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  commonTableField: {
    id: {},
    title: {
      required,
      maxLength: maxLength(100),
    },
    entityFieldName: {
      required,
      maxLength: maxLength(100),
    },
    type: {
      required,
    },
    tableColumnName: {
      required,
      maxLength: maxLength(100),
    },
    columnWidth: {
      numeric,
    },
    order: {},
    editInList: {},
    hideInList: {},
    hideInForm: {},
    enableFilter: {},
    validateRules: {
      maxLength: maxLength(800),
    },
    showInFilterTree: {},
    fixed: {},
    sortable: {},
    treeIndicator: {},
    clientReadOnly: {},
    fieldValues: {
      maxLength: maxLength(2000),
    },
    notNull: {},
    system: {},
    help: {
      maxLength: maxLength(200),
    },
    fontColor: {
      maxLength: maxLength(80),
    },
    backgroundColor: {
      maxLength: maxLength(80),
    },
    nullHideInForm: {},
    endUsed: {},
    options: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class CommonTableFieldUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commonTableFieldService') private commonTableFieldService: () => CommonTableFieldService;
  public commonTableField: ICommonTableField = new CommonTableField();

  @Inject('commonTableService') private commonTableService: () => CommonTableService;

  public commonTables: ICommonTable[] = [];
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
  @Prop(Boolean) showInModal;
  @Prop(Number) commonTableFieldId;
  @Prop(Number) commonTableId;

  public dataContent = [];

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.commonTableFieldId) {
      this.commonTableFieldId = this.$route.params.commonTableFieldId;
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
        Object.assign(that.commonTableField, values);
        if (that.commonTableField.id) {
          this.commonTableFieldService()
            .update(that.commonTableField)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.modelConfigCommonTableField.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              if (this.showInModal) {
                this.$emit('cancel', true);
              } else {
                this.$router.go(-1);
              }
            });
        } else {
          this.commonTableFieldService()
            .create(this.commonTableField)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.modelConfigCommonTableField.created', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'success');
              if (this.showInModal) {
                this.$emit('cancel', true);
              } else {
                this.$router.go(-1);
              }
            });
        }
      })
      .catch(err => {
        this.$message.error('数据获取失败！');
        console.log(err);
      });
  }

  @Watch('commonTableFieldId', { immediate: true })
  public retrieveCommonTableField(commonTableFieldId): void {
    if (!commonTableFieldId) {
      return;
    }
    this.commonTableFieldService()
      .find(commonTableFieldId)
      .subscribe(res => {
        this.commonTableField = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    if (this.showInModal) {
      this.$emit('cancel', false);
    } else {
      this.$router.go(-1);
    }
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.commonTableService().retrieve()]).subscribe(
      ([commonTablesRes]) => {
        this.relationshipsData['commonTables'] = commonTablesRes.data;
        this.relationshipsData['entity'] = this.commonTableField;
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
    if (this.commonTableFieldId || this.updateEntityId) {
      this.retrieveCommonTableField(this.commonTableFieldId || this.updateEntityId);
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
        .findByEntityName('CommonTableField')
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
        if (item.type === 'modalSelect' && this.commonTableField[item.key]) {
          const isArray = Array.isArray(this.commonTableField[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.commonTableField[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.commonTableField[item.key]) {
                const findIds = this.commonTableField[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.commonTableField[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.commonTableField[item.key])) {
                options[propertyName] = this.commonTableField[item.key];
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
    this.relationshipsData['entity'] = this.commonTableField;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.commonTableField)); // loadsh的pick方法
    });
  }
}
