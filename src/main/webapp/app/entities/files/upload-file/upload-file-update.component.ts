import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import moment from 'moment';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import UserService from '@/shared/service/user.service';

import ResourceCategoryService from '../../files/resource-category/resource-category.service';
import { IResourceCategory } from '@/shared/model/files/resource-category.model';

import AlertService from '@/shared/alert/alert.service';
import { IUploadFile, UploadFile } from '@/shared/model/files/upload-file.model';
import UploadFileService from './upload-file.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  uploadFile: {
    id: {},
    fullName: {},
    name: {},
    ext: {},
    type: {},
    url: {},
    path: {},
    folder: {},
    entityName: {},
    createAt: {},
    fileSize: {},
    referenceCount: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class UploadFileUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('uploadFileService') private uploadFileService: () => UploadFileService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public uploadFile: IUploadFile = new UploadFile();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('resourceCategoryService') private resourceCategoryService: () => ResourceCategoryService;

  public resourceCategories: IResourceCategory[] = [];
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
  public uploadFileId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.uploadFileId) {
      this.uploadFileId = this.$route.params.uploadFileId;
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
        Object.assign(that.uploadFile, values);
        if (that.uploadFile.id) {
          this.uploadFileService()
            .update(that.uploadFile)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesUploadFile.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.uploadFileService()
            .create(this.uploadFile)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesUploadFile.created', { param: param.data.id }).toString();
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

  public retrieveUploadFile(uploadFileId): void {
    if (!uploadFileId) {
      return;
    }
    this.uploadFileService()
      .find(uploadFileId)
      .subscribe(res => {
        this.uploadFile = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.userService().retrieve(), this.resourceCategoryService().tree()]).subscribe(
      ([usersRes, resourceCategoriesRes]) => {
        this.relationshipsData['users'] = usersRes.data;
        this.relationshipsData['resourceCategories'] = resourceCategoriesRes.data;
        this.relationshipsData['entity'] = this.uploadFile;
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
    if (this.uploadFileId || this.updateEntityId) {
      this.retrieveUploadFile(this.uploadFileId || this.updateEntityId);
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
        .findByEntityName('UploadFile')
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
        if (item.type === 'modalSelect' && this.uploadFile[item.key]) {
          const isArray = Array.isArray(this.uploadFile[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.uploadFile[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.uploadFile[item.key]) {
                const findIds = this.uploadFile[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.uploadFile[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.uploadFile[item.key])) {
                options[propertyName] = this.uploadFile[item.key];
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
    this.relationshipsData['entity'] = this.uploadFile;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.uploadFile)); // loadsh的pick方法
    });
  }
}
