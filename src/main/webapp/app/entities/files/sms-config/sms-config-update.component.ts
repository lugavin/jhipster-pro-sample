import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";
import AlertService from '@/shared/alert/alert.service';
import { ISmsConfig, SmsConfig } from '@/shared/model/files/sms-config.model';
import SmsConfigService from './sms-config.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  smsConfig: {
    id: {},
    provider: {},
    smsCode: {},
    templateId: {},
    accessKey: {},
    secretKey: {},
    regionId: {},
    signName: {},
    remark: {},
    enabled: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class SmsConfigUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('smsConfigService') private smsConfigService: () => SmsConfigService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public smsConfig: ISmsConfig = new SmsConfig();
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
  public dataContent = [];
  public smsConfigId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.smsConfigId) {
      this.smsConfigId = this.$route.params.smsConfigId;
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
        Object.assign(that.smsConfig, values);
        if (that.smsConfig.id) {
          this.smsConfigService()
            .update(that.smsConfig)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesSmsConfig.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.smsConfigService()
            .create(this.smsConfig)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesSmsConfig.created', { param: param.data.id }).toString();
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

  public retrieveSmsConfig(smsConfigId): void {
    if (!smsConfigId) {
      return;
    }
    this.smsConfigService()
      .find(smsConfigId)
      .subscribe(res => {
        this.smsConfig = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.getData();
  }
  public getData() {
    if (this.smsConfigId || this.updateEntityId) {
      this.retrieveSmsConfig(this.smsConfigId || this.updateEntityId);
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
        .findByEntityName('SmsConfig')
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
        if (item.type === 'modalSelect' && this.smsConfig[item.key]) {
          const isArray = Array.isArray(this.smsConfig[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.smsConfig[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.smsConfig[item.key]) {
                const findIds = this.smsConfig[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.smsConfig[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.smsConfig[item.key])) {
                options[propertyName] = this.smsConfig[item.key];
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
    this.relationshipsData['entity'] = this.smsConfig;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.smsConfig)); // loadsh的pick方法
    });
  }
}
