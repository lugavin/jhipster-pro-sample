import { Component, Inject, Prop, Watch, Ref, Vue } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import moment from 'moment';
import { RelationshipType } from '@/shared/model/enumerations/relationship-type.model';

import CommonTableService from '../../modelConfig//common-table/common-table.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';

import UserService from '@/shared/service/user.service';

import AlertService from '@/shared/alert/alert.service';
import { IStatisticsApi, StatisticsApi } from '@/shared/model/report/statistics-api.model';
import StatisticsApiService from './statistics-api.service';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { quillEditor } from 'vue-quill-editor';
import { UPLOAD_IMAGE_URL } from '@/constants';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';

const validations: any = {
  statisticsApi: {
    title: {
      maxLength: maxLength(200),
    },
    apiKey: {},
    createAt: {},
    updateAt: {},
    sourceType: {},
    apiBody: {},
    result: {},
    updateInterval: {},
    lastSQLRunTime: {},
  },
};

@Component({
  validations,
  components: {
    'jhi-quill-editor': quillEditor,
  },
})
export default class StatisticsApiUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('statisticsApiService') private statisticsApiService: () => StatisticsApiService;
  public statisticsApi: IStatisticsApi = new StatisticsApi();

  @Inject('commonTableService') private commonTableService: () => CommonTableService;

  public commonTables: ICommonTable[] = [];

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
  public statisticsApiId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.statisticsApiId) {
      this.statisticsApiId = this.$route.params.statisticsApiId;
    }
    this.initRelationships();
  }

  public mounted(): void {}

  public getFormValue() {
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(this.statisticsApi, values);
      })
      .catch(() => {
        this.$message.error('数据获取失败！');
        console.log('验证未通过，获取失败');
      });
  }

  public save(): void {
    this.isSaving = true;
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        Object.assign(that.statisticsApi, values);
        if (that.statisticsApi.id) {
          this.statisticsApiService()
            .update(that.statisticsApi)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.statisticsStatisticsApi.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.statisticsApiService()
            .create(that.statisticsApi)
            .subscribe(param => {
              that.isSaving = false;
              const message = this.$t('jhipsterApplicationApp.statisticsStatisticsApi.created', { param: param.data.id }).toString();
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

  public retrieveStatisticsApi(statisticsApiId): void {
    this.statisticsApiService()
      .find(statisticsApiId)
      .subscribe(res => {
        this.statisticsApi = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.commonTableService().retrieve(), this.userService().retrieve()]).subscribe(
      ([commonTablesRes, usersRes]) => {
        this.relationshipsData['commonTables'] = commonTablesRes.data;
        this.relationshipsData['users'] = usersRes.data;
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

  public result() {
    if (this.statisticsApi.id) {
      this.statisticsApiService()
        .result(this.statisticsApi.id)
        .subscribe(res => {
          console.log(res.data);
        });
    } else {
      this.$message.warning('请先保存然后再进行测试');
    }
  }
  public getData() {
    if (this.statisticsApiId || this.updateEntityId) {
      this.retrieveStatisticsApi(this.statisticsApiId || this.updateEntityId);
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
          this.statisticsApi = getDataByFormField(this.formJsonData.list, this.statisticsApi);
          this.$nextTick(() => {
            this.updateForm.setData(this.statisticsApi); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('StatisticsApi')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.statisticsApi = getDataByFormField(this.formJsonData.list, this.statisticsApi);
          this.$nextTick(() => {
            this.updateForm.setData(this.statisticsApi); // loadsh的pick方法
          });
        });
    }
  }
}
