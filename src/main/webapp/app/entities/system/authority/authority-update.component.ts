import { Component, Vue, Inject, Prop, Watch, Ref } from 'vue-property-decorator';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import UserService from '@/shared/service/user.service';
import ViewPermissionService from '../../system//view-permission/view-permission.service';
import { IViewPermission } from '@/shared/model/system/view-permission.model';
import AlertService from '@/shared/alert/alert.service';
import { IAuthority, Authority } from '@/shared/model/system/authority.model';
import AuthorityService from './authority.service';
import { forkJoin } from 'rxjs';
import { generateDataForDesigner, getDataByFormField, idObjectArrayToIdArray, idsToIdObjectArray } from '@/utils/entity-form-utils';

const validations: any = {
  authority: {
    name: {},
    code: {},
    info: {},
    order: {},
    display: {},
  },
};

@Component({
  validations,
  components: {},
})
export default class AuthorityUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('authorityService') private authorityService: () => AuthorityService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  public authority: IAuthority = new Authority();

  public authorities: IAuthority[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('viewPermissionService') private viewPermissionService: () => ViewPermissionService;

  public viewPermissions: IViewPermission[] = [];
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
  public authorityId = null;

  created(): void {
    // 判断是否从路由中获得实体id
    if (this.$route.params.authorityId) {
      this.authorityId = this.$route.params.authorityId;
    }
    this.initRelationships();
    this.authority.users = [];
    this.authority.viewPermissions = [];
  }

  public mounted(): void {}

  public save(): void {
    this.isSaving = true;
    const that = this;
    this.updateForm
      .getData()
      .then(values => {
        that.authority = Object.assign(that.authority, values);
        that.authority.users = idsToIdObjectArray(that.authority['users']);
        that.authority.viewPermissions = idsToIdObjectArray(that.authority['viewPermissions']);
        if (that.authority.id) {
          this.authorityService()
            .update(that.authority)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('test037App.systemAuthority.updated', { param: param.data.id }).toString();
              this.alertService().showAlert(message, 'info');
              this.$router.go(-1);
            });
        } else {
          this.authorityService()
            .create(that.authority)
            .subscribe(param => {
              this.isSaving = false;
              const message = this.$t('test037App.systemAuthority.created', { param: param.data.id }).toString();
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

  public retrieveAuthority(authorityId): void {
    this.authorityService()
      .find(authorityId)
      .subscribe(res => {
        this.authority = res.data;
        this.authority.users = idObjectArrayToIdArray(this.authority.users);
        this.authority.viewPermissions = idObjectArrayToIdArray(this.authority.viewPermissions);
        this.getFormData();
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.userService().retrieve(), this.viewPermissionService().tree(), this.authorityService().tree()]).subscribe(
      ([usersRes, viewPermissionsRes, authoritiesRes]) => {
        this.relationshipsData['users'] = usersRes.data;
        this.relationshipsData['viewPermissions'] = viewPermissionsRes.data;
        this.relationshipsData['authorities'] = authoritiesRes.data;
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
    if (this.authorityId || this.updateEntityId) {
      this.retrieveAuthority(this.authorityId || this.updateEntityId);
    } else {
      this.getFormData();
    }
  }
  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
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
          this.authority = getDataByFormField(this.formJsonData.list, this.authority);
          this.$nextTick(() => {
            this.updateForm.setData(this.authority); // loadsh的pick方法
          });
        });
    } else {
      this.commonTableService()
        .findByEntityName('Authority')
        .subscribe(res => {
          const commonTableData = res.data;
          if (commonTableData.formConfig && commonTableData.formConfig.length > 0) {
            this.formJsonData = JSON.parse(commonTableData.formConfig);
          } else {
            this.formJsonData.list = generateDataForDesigner(commonTableData);
          }
          this.authority = getDataByFormField(this.formJsonData.list, this.authority);
          this.$nextTick(() => {
            this.updateForm.setData(this.authority); // loadsh的pick方法
          });
        });
    }
  }
}
