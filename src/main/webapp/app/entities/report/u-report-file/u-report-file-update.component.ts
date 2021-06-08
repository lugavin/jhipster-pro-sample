import { Component, Inject, Prop, Watch, Ref, Vue } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import CommonTableService from '../../modelConfig//common-table/common-table.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';

import AlertService from '@/shared/alert/alert.service';
import { IUReportFile, UReportFile } from '@/shared/model/report/u-report-file.model';
import UReportFileService from './u-report-file.service';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { quillEditor } from 'vue-quill-editor';
import { UPLOAD_IMAGE_URL } from '@/constants';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  uReportFile: {
    name: {},
    content: {},
    createAt: {},
    updateAt: {},
  },
};

@Component({
  validations,
  components: {
    'jhi-quill-editor': quillEditor,
  },
})
export default class UReportFileUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('uReportFileService') private uReportFileService: () => UReportFileService;
  public uReportFile: IUReportFile = new UReportFile();
  @Prop(Boolean) showInModal;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;

  public commonTables: ICommonTable[] = [];
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
  public uReportFileId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.uReportFileId) {
      this.uReportFileId = this.$route.params.uReportFileId;
    }
    this.initRelationships();
  }

  public mounted(): void {}

  public getFormValue() {
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(this.uReportFile, values);
      })
      .catch(() => {
        this.$message.error('数据获取失败！');
        console.log('验证未通过，获取失败');
      });
  }

  public save(): void {
    this.getFormValue();
    this.isSaving = true;
    if (this.uReportFile.id) {
      this.uReportFileService()
        .update(this.uReportFile)
        .subscribe(param => {
          this.isSaving = false;
          const message = this.$t('jhipsterApplicationApp.reportUReportFile.updated', { param: param.data.id }).toString();
          this.alertService().showAlert(message, 'info');
          if (this.showInModal) {
            this.$emit('cancel', true);
          } else {
            this.$router.go(-1);
          }
        });
    } else {
      this.uReportFileService()
        .create(this.uReportFile)
        .subscribe(param => {
          this.isSaving = false;
          const message = this.$t('jhipsterApplicationApp.reportUReportFile.created', { param: param.data.id }).toString();
          this.alertService().showAlert(message, 'success');
          if (this.showInModal) {
            this.$emit('cancel', true);
          } else {
            this.$router.go(-1);
          }
        });
    }
  }

  public retrieveUReportFile(uReportFileId): void {
    this.uReportFileService()
      .find(uReportFileId)
      .subscribe(res => {
        this.uReportFile = res.data;
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
    forkJoin([this.commonTableService().retrieve()]).subscribe(
      ([commonTablesRes]) => {
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
    if (this.uReportFileId) {
      this.retrieveUReportFile(this.uReportFileId);
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
          this.uReportFile = getDataByFormField(this.formJsonData.list, this.uReportFile);
          this.$nextTick(() => {
            this.updateForm.setData(this.uReportFile); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('UReportFile')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.uReportFile = getDataByFormField(this.formJsonData.list, this.uReportFile);
          this.$nextTick(() => {
            this.updateForm.setData(this.uReportFile); // loadsh的pick方法
          });
        });
    }
  }
}
