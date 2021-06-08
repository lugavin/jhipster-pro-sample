import { Component, Inject, Prop, Ref, Watch, Vue } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

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
import { generateDataForDesigner } from '@/utils/entity-form-utils';

const validations: any = {
  commonTable: {
    name: {},
    entityName: {},
    tableName: {},
    system: {},
    generated: {},
    creatAt: {},
    generateAt: {},
    generateClassAt: {},
    description: {},
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
export default class DesignFormComponent extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  @Inject('commonTableFieldService') private commonTableFieldService: () => CommonTableFieldService;
  @Inject('commonTableRelationshipService') private commonTableRelationshipService: () => CommonTableRelationshipService;
  @Inject('userService') private userService: () => UserService;
  @Inject('businessTypeService') private businessTypeService: () => BusinessTypeService;

  // designer增加内容
  @Ref('designer') designer: any;
  public commonTableId = null;

  public commonTable: ICommonTable = new CommonTable();
  public commonTableFields: ICommonTableField[] = [];
  public commonTableRelationships: ICommonTableRelationship[] = [];
  public users: Array<any> = [];
  public businessTypes: IBusinessType[] = [];
  public isSaving = false;
  public designerData = {
    list: [],
    config: {
      layout: 'horizontal',
      labelCol: { span: 4 },
      wrapperCol: { span: 18 },
      hideRequiredMark: false,
      customStyle: '',
    },
  };
  public dataFormContent = [];
  public dataContent = [];

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initRelationships();
      if (to.params.commonTableId) {
        vm.commonTableId = to.params.commonTableId;
        vm.getDesignerData(to.params.commonTableId);
      }
    });
  }

  public mounted(): void {}

  public save(): void {
    this.isSaving = true;
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
  }

  public retrieveCommonTable(commonTableId): void {
    this.commonTableService()
      .find(commonTableId)
      .subscribe(res => {
        this.commonTable = res.data;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.commonTableFieldService()
      .retrieve()
      .subscribe(res => {
        this.commonTableFields = res.data;
      });
    this.commonTableRelationshipService()
      .retrieve()
      .subscribe(res => {
        this.commonTableRelationships = res.data;
      });
    this.userService()
      .retrieve()
      .subscribe(res => {
        this.users = res.data;
      });
    this.businessTypeService()
      .retrieve()
      .subscribe(res => {
        this.businessTypes = res.data;
      });
  }

  public getDesignerData(id: number) {
    this.commonTableService()
      .find(id)
      .subscribe(
        res => {
          if (res.data.formConfig) {
            const formData = JSON.parse(res.data.formConfig);
            if (formData.list && formData.list.length > 0) {
              this.importData(formData);
            } else {
              this.designerData.list = generateDataForDesigner(res.data);
              this.importData(this.designerData);
            }
          } else {
            this.designerData.list = generateDataForDesigner(res.data);
            this.importData(this.designerData);
          }
        },
        error => {
          this.$message.error({
            content: `数据获取失败。信息：${error.message}`,
            onClose: () => {},
          });
        }
      );
  }

  // ----- 以下为designer增加内容

  public importData(formData) {
    this.designer.handleSetData(formData);
  }

  public closeDesign() {
    (<any>this).$router.go(-1);
  }

  public saveToServer(formConfig) {
    const commonTable = { id: this.commonTableId, formConfig };
    this.commonTableService()
      .updateBySpecifiedField(commonTable, 'formConfig')
      .subscribe(
        res => {
          this.$message.success('成功保存数据到服务器。');
        },
        error => {
          this.$message.error({
            content: `数据获取失败。信息：${error.message}`,
            onClose: () => {},
          });
        }
      );
  }

  /**
   * 从数据库重新生成JsonData数据并导入。
   */
  public rebuildFromServer() {
    this.commonTableService()
      .find(this.commonTableId)
      .subscribe(
        res => {
          this.designerData.list = generateDataForDesigner(res.data);
          this.importData(this.designerData);
        },
        error => {
          this.$message.error({
            content: `数据获取失败。信息：${error.message}`,
            onClose: () => {},
          });
        }
      );
  }
}
