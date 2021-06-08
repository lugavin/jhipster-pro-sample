import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import CommonConditionService from '../../report/common-condition/common-condition.service';
import { ICommonCondition } from '@/shared/model/report/common-condition.model';

import AlertService from '@/shared/alert/alert.service';
import { ICommonConditionItem, CommonConditionItem } from '@/shared/model/report/common-condition-item.model';
import CommonConditionItemService from './common-condition-item.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  commonConditionItem: {
    id: {},
    prefix: {},
    fieldName: {},
    fieldType: {},
    operator: {},
    value: {},
    suffix: {},
    order: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class CommonConditionItemUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commonConditionItemService') private commonConditionItemService: () => CommonConditionItemService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public commonConditionItem: ICommonConditionItem = new CommonConditionItem();

  @Inject('commonConditionService') private commonConditionService: () => CommonConditionService;

  public commonConditions: ICommonCondition[] = [];
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
  @Prop(Number) commonConditionItemId;
  @Prop(Number) commonConditionId;

  public dataContent = [];

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.commonConditionItemId) {
      this.commonConditionItemId = this.$route.params.commonConditionItemId;
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
        Object.assign(that.commonConditionItem, values);
        if (that.commonConditionItem.id) {
          this.commonConditionItemService()
            .update(that.commonConditionItem)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.reportCommonConditionItem.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              if (this.showInModal) {
                this.$emit('cancel', true);
              } else {
                this.$router.go(-1);
              }
            });
        } else {
          this.commonConditionItemService()
            .create(this.commonConditionItem)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.reportCommonConditionItem.created', { param: param.data.id }).toString();
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

  @Watch('commonConditionItemId', { immediate: true })
  public retrieveCommonConditionItem(commonConditionItemId): void {
    if (!commonConditionItemId) {
      return;
    }
    this.commonConditionItemService()
      .find(commonConditionItemId)
      .subscribe(res => {
        this.commonConditionItem = res.data;
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
    forkJoin([this.commonConditionService().retrieve()]).subscribe(
      ([commonConditionsRes]) => {
        this.relationshipsData['commonConditions'] = commonConditionsRes.data;
        this.relationshipsData['entity'] = this.commonConditionItem;
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
    if (this.commonConditionItemId || this.updateEntityId) {
      this.retrieveCommonConditionItem(this.commonConditionItemId || this.updateEntityId);
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
        .findByEntityName('CommonConditionItem')
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
        if (item.type === 'modalSelect' && this.commonConditionItem[item.key]) {
          const isArray = Array.isArray(this.commonConditionItem[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.commonConditionItem[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.commonConditionItem[item.key]) {
                const findIds = this.commonConditionItem[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.commonConditionItem[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.commonConditionItem[item.key])) {
                options[propertyName] = this.commonConditionItem[item.key];
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
    this.relationshipsData['entity'] = this.commonConditionItem;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.commonConditionItem)); // loadsh的pick方法
    });
  }
}
