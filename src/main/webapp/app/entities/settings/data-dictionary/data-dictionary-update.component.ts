import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import AlertService from '@/shared/alert/alert.service';
import { IDataDictionary, DataDictionary } from '@/shared/model/settings/data-dictionary.model';
import DataDictionaryService from './data-dictionary.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  dataDictionary: {
    id: {},
    name: {},
    code: {},
    description: {},
    fontColor: {},
    backgroundColor: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class DataDictionaryUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('dataDictionaryService') private dataDictionaryService: () => DataDictionaryService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public dataDictionary: IDataDictionary = new DataDictionary();

  public dataDictionaries: IDataDictionary[] = [];
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
  public dataDictionaryId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.dataDictionaryId) {
      this.dataDictionaryId = this.$route.params.dataDictionaryId;
    }
    if (this.$route.params.parentId) {
      this.dataDictionary.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.dataDictionary.parent.id = this.parentId;
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
        Object.assign(that.dataDictionary, values);
        if (that.dataDictionary.id) {
          this.dataDictionaryService()
            .update(that.dataDictionary)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsDataDictionary.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.dataDictionaryService()
            .create(this.dataDictionary)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsDataDictionary.created', { param: param.data.id }).toString();
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

  public retrieveDataDictionary(dataDictionaryId): void {
    if (!dataDictionaryId) {
      return;
    }
    this.dataDictionaryService()
      .find(dataDictionaryId)
      .subscribe(res => {
        this.dataDictionary = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.dataDictionaryService().tree()]).subscribe(
      ([dataDictionariesRes]) => {
        this.relationshipsData['dataDictionaries'] = dataDictionariesRes.data;
        this.relationshipsData['entity'] = this.dataDictionary;
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
    if (this.dataDictionaryId || this.updateEntityId) {
      this.retrieveDataDictionary(this.dataDictionaryId || this.updateEntityId);
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
        .findByEntityName('DataDictionary')
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
        if (item.type === 'modalSelect' && this.dataDictionary[item.key]) {
          const isArray = Array.isArray(this.dataDictionary[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.dataDictionary[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.dataDictionary[item.key]) {
                const findIds = this.dataDictionary[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.dataDictionary[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.dataDictionary[item.key])) {
                options[propertyName] = this.dataDictionary[item.key];
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
    this.relationshipsData['entity'] = this.dataDictionary;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.dataDictionary)); // loadsh的pick方法
    });
  }
}
