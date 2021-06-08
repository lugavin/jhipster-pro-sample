import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
// import { ICommonTable } from "@/shared/model/modelConfig/common-table.model";
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
// import { FieldType } from "@/shared/model/modelConfig/common-table-field.model";
// import { RelationshipType } from "@/shared/model/modelConfig/common-table-relationship.model";

import UploadFileService from '../../files/upload-file/upload-file.service';
import { IUploadFile } from '@/shared/model/files/upload-file.model';

import UploadImageService from '../../files/upload-image/upload-image.service';
import { IUploadImage } from '@/shared/model/files/upload-image.model';

import AlertService from '@/shared/alert/alert.service';
import { IResourceCategory, ResourceCategory } from '@/shared/model/files/resource-category.model';
import ResourceCategoryService from './resource-category.service';
import { UPLOAD_IMAGE_URL, UPLOAD_FILE_URL } from '@/constants';
import { forkJoin } from 'rxjs';
import lowerFirst from 'lodash/lowerFirst';
import { idObjectArrayToIdArray, idsToIdObjectArray, generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  resourceCategory: {
    id: {},
    title: {
      maxLength: maxLength(40),
    },
    code: {
      maxLength: maxLength(20),
    },
    sort: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class ResourceCategoryUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('resourceCategoryService') private resourceCategoryService: () => ResourceCategoryService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public resourceCategory: IResourceCategory = new ResourceCategory();

  @Inject('uploadFileService') private uploadFileService: () => UploadFileService;

  public uploadFiles: IUploadFile[] = [];
  public filesFileList: any[] = [];

  public resourceCategories: IResourceCategory[] = [];

  @Inject('uploadImageService') private uploadImageService: () => UploadImageService;

  public uploadImages: IUploadImage[] = [];
  public imagesFileList: any[] = [];
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
  public resourceCategoryId = null;
  public authHeader = { Authorization: 'Bearer ' };
  public uploadFileUrl = UPLOAD_FILE_URL;
  public uploadImageUrl = UPLOAD_IMAGE_URL;
  public previewImage: string | undefined = '';
  public previewVisible = false;
  public showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true,
  };

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.resourceCategoryId) {
      this.resourceCategoryId = this.$route.params.resourceCategoryId;
    }
    if (this.$route.params.parentId) {
      this.resourceCategory.parent.id = Number(this.$route.params.parentId);
    }
    if (this.parentId) {
      this.resourceCategory.parent.id = this.parentId;
    }
    this.initRelationships();
  }

  public mounted(): void {
    const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
    this.authHeader.Authorization = 'Bearer ' + token;
  }

  public save(): void {
    this.isSaving = true;
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(that.resourceCategory, values);
        if (that.resourceCategory.id) {
          this.resourceCategoryService()
            .update(that.resourceCategory)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesResourceCategory.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.resourceCategoryService()
            .create(this.resourceCategory)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.filesResourceCategory.created', { param: param.data.id }).toString();
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

  public retrieveResourceCategory(resourceCategoryId): void {
    if (!resourceCategoryId) {
      return;
    }
    this.resourceCategoryService()
      .find(resourceCategoryId)
      .subscribe(res => {
        this.resourceCategory = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.resourceCategoryService().tree()]).subscribe(
      ([resourceCategoriesRes]) => {
        this.relationshipsData['resourceCategories'] = resourceCategoriesRes.data;
        this.relationshipsData['entity'] = this.resourceCategory;
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
    if (this.resourceCategoryId || this.updateEntityId) {
      this.retrieveResourceCategory(this.resourceCategoryId || this.updateEntityId);
    } else {
      this.getFormData();
    }
  }
  public beforeUpload(file) {
    const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
    if (!isJPG) {
      this.$message.error('You can only upload JPG file!');
    }
    const isLt2M = file.size / 1024 / 1024 < 4;
    if (!isLt2M) {
      this.$message.error('Image must smaller than 4MB!');
    }
    return isJPG && isLt2M;
  }
  public getBase64(img, callback) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
  }
  private checkImageDimension(file: File): Promise<boolean> {
    return new Promise(resolve => {
      const img = new Image(); // create image
      img.src = window.URL.createObjectURL(file);
      img.onload = () => {
        const width = img.naturalWidth;
        const height = img.naturalHeight;
        window.URL.revokeObjectURL(img.src);
        resolve(width === height && width >= 3000);
      };
    });
  }
  public handlePreview(file) {
    this.previewImage = file.url ?? file.thumbUrl;
    this.previewVisible = true;
  }
  public handlePreviewCancel() {
    this.previewVisible = false;
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
        .findByEntityName('ResourceCategory')
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
        if (item.type === 'modalSelect' && this.resourceCategory[item.key]) {
          const isArray = Array.isArray(this.resourceCategory[item.key]);
          const options = {};
          const propertyName = isArray ? 'id.in' : 'id.equals';
          // options[propertyName] = this.resourceCategory[item.key];
          if (isArray) {
            if (this.relationshipsData[item.options.dynamicKey]) {
              if (this.resourceCategory[item.key]) {
                const findIds = this.resourceCategory[item.key].filter(idItem => {
                  return !this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === idItem);
                });
                if (findIds.length > 0) {
                  options[propertyName] = findIds;
                }
              }
            }
          } else {
            if (this.resourceCategory[item.key]) {
              if (!this.relationshipsData[item.options.dynamicKey].some(dataItem => dataItem.id === this.resourceCategory[item.key])) {
                options[propertyName] = this.resourceCategory[item.key];
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
    this.relationshipsData['entity'] = this.resourceCategory;
    this.$nextTick(() => {
      this.updateForm.setData(getDataByFormField(this.formJsonData.list, this.resourceCategory)); // loadsh的pick方法
    });
  }
}
