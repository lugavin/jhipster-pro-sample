import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import UserService from '@/shared/service/user.service';

import AlertService from '@/shared/alert/alert.service';
import { IPosition, Position } from '@/shared/model/settings/position.model';
import PositionService from './position.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  position: {
    id: {},
    code: {
      required,
      maxLength: maxLength(50),
    },
    name: {
      required,
      maxLength: maxLength(50),
    },
    sortNo: {},
    description: {
      maxLength: maxLength(200),
    },
  },
};

@Component({
  validations,
  components: {},
})
export default class PositionUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('positionService') private positionService: () => PositionService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public position: IPosition = new Position();

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
  @Prop(Boolean) showInModal;
  public dataContent = [];
  public positionId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.positionId) {
      this.positionId = this.$route.params.positionId;
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
        Object.assign(that.position, values);
        if (that.position.id) {
          this.positionService()
            .update(that.position)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsPosition.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.positionService()
            .create(this.position)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.settingsPosition.created', { param: param.data.id }).toString();
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

  public retrievePosition(positionId): void {
    if (!positionId) {
      return;
    }
    this.positionService()
      .find(positionId)
      .subscribe(res => {
        this.position = res.data;
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
    if (this.positionId || this.updateEntityId) {
      this.retrievePosition(this.positionId || this.updateEntityId);
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
        .findByEntityName('Position')
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
        if (item.type === 'modalSelect' && this.position[item.key]) {
          const isArray = Array.isArray(this.position[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.position[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.position[item.key]) {
                const findIds = this.position[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.position[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.position[item.key])) {
                options[propertyName] = this.position[item.key];
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
    this.relationshipsData['entity'] = this.position;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.position)); // loadsh的pick方法
    });
  }
}
