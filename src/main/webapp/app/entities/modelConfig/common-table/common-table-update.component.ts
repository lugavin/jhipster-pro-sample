import { Component, Inject, Prop, Watch, Ref, Vue } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import CommonTableFieldService from '../../modelConfig//common-table-field/common-table-field.service';
import { ICommonTableField } from '@/shared/model/modelConfig/common-table-field.model';

import CommonTableRelationshipService from '../../modelConfig//common-table-relationship/common-table-relationship.service';
import { ICommonTableRelationship } from '@/shared/model/modelConfig/common-table-relationship.model';

import UserService from '@/shared/service/user.service';

import BusinessTypeService from '../../settings/business-type/business-type.service';
import { IBusinessType } from '@/shared/model/settings/business-type.model';

import AlertService from '@/shared/alert/alert.service';
import { ICommonTable, CommonTable } from '@/shared/model/modelConfig/common-table.model';
import CommonTableService from './common-table.service';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { quillEditor } from 'vue-quill-editor';
import { UPLOAD_IMAGE_URL } from '@/constants';
import CommonTableFieldComponent from '../../modelConfig//common-table-field/common-table-field.vue';
import CommonTableRelationshipComponent from '../../modelConfig//common-table-relationship/common-table-relationship.vue';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  commonTable: {
    name: {
      required,
      maxLength: maxLength(80),
    },
    entityName: {
      required,
      maxLength: maxLength(80),
    },
    tableName: {
      required,
      maxLength: maxLength(80),
    },
    system: {},
    clazzName: {
      required,
      maxLength: maxLength(80),
    },
    generated: {},
    creatAt: {},
    generateAt: {},
    generateClassAt: {},
    description: {
      maxLength: maxLength(200),
    },
    treeTable: {},
    baseTableId: {},
    listConfig: {},
    formConfig: {},
  },
};

@Component({
  validations,
  components: {
    'jhi-quill-editor': quillEditor,
    'jhi-common-table-field': CommonTableFieldComponent,
    'jhi-common-table-relationship': CommonTableRelationshipComponent,
  },
})
export default class CommonTableUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public commonTable: ICommonTable = new CommonTable();

  @Inject('commonTableFieldService') private commonTableFieldService: () => CommonTableFieldService;

  public commonTableFields: ICommonTableField[] = [];

  @Inject('commonTableRelationshipService') private commonTableRelationshipService: () => CommonTableRelationshipService;

  public commonTableRelationships: ICommonTableRelationship[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('businessTypeService') private businessTypeService: () => BusinessTypeService;

  public businessTypes: IBusinessType[] = [];
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
  public dataContent = [];
  public commonTableId = null;
  public copyFromId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.commonTableId) {
      this.commonTableId = this.$route.params.commonTableId;
    }
    if (this.$route.params.copyFromId) {
      this.copyFromId = this.$route.params.copyFromId;
    }
    this.initRelationships();
  }

  public mounted(): void {}

  public save(): void {
    this.isSaving = true;
    this.updateForm
      .getData()
      .then(values => {
        this.commonTable = Object.assign(this.commonTable, values);
        if (this.commonTable.id) {
          this.commonTableService()
            .update(this.commonTable)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.modelConfigCommonTable.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.commonTableService()
            .create(this.commonTable)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.modelConfigCommonTable.created', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'success');
              this.$router.go(-1);
            });
        }
      })
      .catch(() => {
        this.$message.error('数据更新失败！');
        console.log('验证未通过，获取失败');
      });
  }

  public retrieveCommonTable(commonTableId): void {
    this.commonTableService()
      .find(commonTableId)
      .subscribe(res => {
        this.commonTable = res.data;
        this.getFormData();
      });
  }

  public retrieveCopiedCommonTable(copyFromId): void {
    this.commonTableService()
      .copy(copyFromId)
      .subscribe(res => {
        this.commonTable = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.userService().retrieve(), this.businessTypeService().retrieve()]).subscribe(
      ([usersRes, businessTypesRes]) => {
        this.relationshipsData['users'] = usersRes.data;
        this.relationshipsData['businessTypes'] = businessTypesRes.data;
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
    if (this.commonTableId) {
      this.retrieveCommonTable(this.commonTableId);
    } else if (this.copyFromId) {
      this.retrieveCopiedCommonTable(this.copyFromId);
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
          this.commonTable = getDataByFormField(this.formJsonData.list, this.commonTable);
          this.$nextTick(() => {
            this.updateForm.setData(this.commonTable); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('CommonTable')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.commonTable = getDataByFormField(this.formJsonData.list, this.commonTable);
          this.$nextTick(() => {
            this.updateForm.setData(this.commonTable); // loadsh的pick方法
          });
        });
    }
  }
}
