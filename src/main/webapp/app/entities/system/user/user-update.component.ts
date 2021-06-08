import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import UserService from '@/shared/service/user.service';
import AlertService from '@/shared/alert/alert.service';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField } from '@/utils/entity-form-utils';
import { IUser, User } from '@/shared/model/user.model';

const validations: any = {
  person: {
    birthday: {},
    mobile: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class PersonUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('userService') private userService: () => UserService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public user: IUser = new User();

  public users: Array<any> = [];
  @Prop(Number) readonly personId: number | undefined;
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
  public userId = null;

  created(): void {
    if (this.$route.params.userId) {
      this.userId = this.$route.params.userId;
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
        that.user = Object.assign(that.user, values);
        if (that.user.id) {
          this.userService()
            .update(that.user)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('testmariadb3App.systemPerson.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.userService()
            .create(that.user)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('testmariadb3App.systemPerson.created', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'success');
              this.$router.go(-1);
            });
        }
      })
      .catch(err => {
        this.$message.error('更新失败');
        console.log(err);
      });
  }

  public retrievePerson(userId): void {
    this.userService()
      .get(userId)
      .subscribe(res => {
        this.user = res.data;
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.userService().retrieve()]).subscribe(
      ([usersRes]) => {
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
  public getData() {
    if (this.userId) {
      this.retrievePerson(this.userId);
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
          this.user = getDataByFormField(this.formJsonData.list, this.user);
          this.$nextTick(() => {
            this.updateForm.setData(this.user); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('User')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.user = getDataByFormField(this.formJsonData.list, this.user);
          this.$nextTick(() => {
            this.updateForm.setData(this.user); // loadsh的pick方法
          });
        });
    }
  }
}
