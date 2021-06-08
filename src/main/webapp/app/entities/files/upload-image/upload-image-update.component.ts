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
import { IUploadImage, UploadImage } from '@/shared/model/files/upload-image.model';
import UploadImageService from './upload-image.service';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  uploadImage: {
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
    smartUrl: {},
    mediumUrl: {},
    referenceCount: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class UploadImageUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('uploadImageService') private uploadImageService: () => UploadImageService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public uploadImage: IUploadImage = new UploadImage();

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
  public uploadImageId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.uploadImageId) {
      this.uploadImageId = this.$route.params.uploadImageId;
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
        Object.assign(that.uploadImage, values);
        if (that.uploadImage.id) {
          this.uploadImageService()
            .update(that.uploadImage)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesUploadImage.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.uploadImageService()
            .create(this.uploadImage)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesUploadImage.created', { param: param.data.id }).toString();
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

  public retrieveUploadImage(uploadImageId): void {
    if (!uploadImageId) {
      return;
    }
    this.uploadImageService()
      .find(uploadImageId)
      .subscribe(res => {
        this.uploadImage = res.data;
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
        this.relationshipsData['entity'] = this.uploadImage;
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
    if (this.uploadImageId || this.updateEntityId) {
      this.retrieveUploadImage(this.uploadImageId || this.updateEntityId);
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
        .findByEntityName('UploadImage')
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
        if (item.type === 'modalSelect' && this.uploadImage[item.key]) {
          const isArray = Array.isArray(this.uploadImage[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.uploadImage[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.uploadImage[item.key]) {
                const findIds = this.uploadImage[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.uploadImage[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.uploadImage[item.key])) {
                options[propertyName] = this.uploadImage[item.key];
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
    this.relationshipsData['entity'] = this.uploadImage;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.uploadImage)); // loadsh的pick方法
    });
  }
}
