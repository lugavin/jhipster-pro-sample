import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';
import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import CommonConditionItemService from '../common-condition-item/common-condition-item.service';
import { ICommonConditionItem } from '@/shared/model/report/common-condition-item.model';
import UserService from '@/shared/service/user.service';
import CommonTableService from '../../modelConfig//common-table/common-table.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import AlertService from '@/shared/alert/alert.service';
import { ICommonCondition, CommonCondition } from '@/shared/model/report/common-condition.model';
import CommonConditionService from './common-condition.service';
import CommonConditionItemComponent from '../common-condition-item/common-condition-item.vue';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  commonQuery: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    description: {},
    lastModifiedTime: {},
  },
};

@Component({
  validations,
  components: {
    'jhi-common-condition-item': CommonConditionItemComponent,
  },
})
export default class CommonConditionUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commonConditionService') private commonConditionService: () => CommonConditionService;
  public commonCondition: ICommonCondition = new CommonCondition();

  @Inject('commonConditionItemService') private commonConditionItemService: () => CommonConditionItemService;

  public commonConditionItems: ICommonConditionItem[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('commonTableService') private commonTableService: () => CommonTableService;

  public commonTables: ICommonTable[] = [];
  public isSaving = false;
  public loading = false;
  @Ref('updateForm') readonly updateForm: any;
  @Ref('itemListComponent') readonly itemListComponent: any;
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
  public commonConditionId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.commonConditionId) {
      this.commonConditionId = this.$route.params.commonConditionId;
    }
    this.initRelationships();
  }

  public mounted(): void {}

  public getFormValue() {}

  public save(): void {
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(that.commonCondition, values);
        this.isSaving = true;
        this.itemListComponent.saveAll();
        if (that.commonCondition.id) {
          this.commonConditionService()
            .update(that.commonCondition)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.commonQueryCommonQuery.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.commonConditionService()
            .create(that.commonCondition)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.commonQueryCommonQuery.created', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'success');
              this.$router.go(-1);
            });
        }
      })
      .catch(error => {
        this.$message.error('数据获取失败！');
        console.log(error);
      });
  }

  public retrieveCommonQuery(commonConditionId): void {
    this.commonConditionService()
      .find(commonConditionId)
      .subscribe(res => {
        this.commonCondition = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.userService().retrieve(), this.commonTableService().retrieve()]).subscribe(
      ([usersRes, commonTablesRes]) => {
        this.relationshipsData['users'] = usersRes.data;
        this.relationshipsData['commonTables'] = commonTablesRes.data;
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
    if (this.commonConditionId || this.updateEntityId) {
      this.retrieveCommonQuery(this.commonConditionId || this.updateEntityId);
    } else {
      this.getFormData();
    }
  }
  public getFormData(formDataId?: number) {
    if (formDataId) {
      this.commonTableService()
        .find(formDataId)
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.commonCondition = getDataByFormField(this.formJsonData.list, this.commonCondition);
          this.$nextTick(() => {
            this.updateForm.setData(this.commonCondition); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('CommonCondition')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.commonCondition = getDataByFormField(this.formJsonData.list, this.commonCondition);
          this.$nextTick(() => {
            this.updateForm.setData(this.commonCondition); // loadsh的pick方法
          });
        });
    }
  }
}
