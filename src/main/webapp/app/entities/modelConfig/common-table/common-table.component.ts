import { mixins } from 'vue-class-component';
import { Vue, Component, Inject, Prop, Ref } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICommonTable, CommonTable } from '@/shared/model/modelConfig/common-table.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import JhiDataUtils from '@/shared/data/data-utils.service';

import CommonTableService from './common-table.service';
import { IUser } from '@/shared/model/user.model';
import UserService from '@/shared/service/user.service';
import { IBusinessType } from '@/shared/model/settings/business-type.model';
import BusinessTypeService from '@/entities/settings/business-type/business-type.service';
import CommonConditionService from '@/entities/report/common-condition/common-condition.service';
import { ICommonCondition } from '@/shared/model/report/common-condition.model';
import { AxiosResponse } from 'axios';
import { getFilter, xGenerateFilterTree, xGenerateTableColumns } from '@/utils/entity-list-utils';
import { forkJoin } from 'rxjs';

@Component
export default class CommonTableComponent extends Vue {
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  @Inject('userService') private userService: () => UserService;
  @Inject('businessTypeService') private businessTypeService: () => BusinessTypeService;
  @Inject('commonConditionService') private commonConditionService: () => CommonConditionService;
  @Ref() public searchInput;
  @Ref('xGrid') public xGrid;
  public xGridData = [];
  public xGridColumns = [];
  public xGridTableToolbars = {
    perfect: true,
    custom: true,
    slots: {
      buttons: 'toolbar_buttons',
    },
  };
  public xGridSelectRecords = [];
  private loading: boolean = false;
  public relationshipsData: any = {};
  public xGridPagerConfig = {
    layouts: ['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total'],
    pageSize: 15,
    pageSizes: [5, 10, 15, 20, 30, 50],
    total: 0,
    pagerCount: 5,
    currentPage: 1,
  };
  public xGridSortConfig = {
    trigger: 'default',
    defaultSort: {
      field: 'id',
      order: 'asc',
    },
  };
  public xGridFilterConfig = {
    remote: true,
  };

  private removeId: number = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public filterTreeSpan = 0;
  @Prop(Object) otherPresetOrder: { [key: string]: any };
  public treeFilterData = [];
  public expandedKeys = [];
  public autoExpandParent = true;
  public checkedKeys = [];
  public selectedKeys = [];
  public mapOfSort: { [key: string]: any } = {};
  public reverse = false;
  public totalItems = 0;
  public omitFields = ['id', 'listConfig', 'formConfig'];
  public commonTables: ICommonTable[] = [];
  public mapOfFilter: { [key: string]: any } = {
    commonTableFields: { list: [], value: [], type: 'one-to-many' },
    relationships: { list: [], value: [], type: 'one-to-many' },
    creator: { list: [], value: [], type: 'many-to-one' },
    businessType: { list: [], value: [], type: 'many-to-one' },
  };
  public editStatus: { [key: string]: any } = {};
  public isFetching = false;
  public searchValue = '';
  public commonConditions: ICommonCondition[] = [];
  public selectCommonConditionId: number = null;
  public users: IUser[];
  public businesstypes: IBusinessType[];
  public showCommonConditionModal = false;

  public created(): void {
    this.initRelationships();
  }

  public mounted(): void {
    this.loadAll();
  }

  public clear(): void {
    this.xGridPagerConfig.currentPage = 1;
    this.loadAll();
  }
  public loadAll(): void {
    this.loading = true;

    const paginationQuery = {
      listModelName: 'CommonTable',
      page: this.xGridPagerConfig.currentPage - 1,
      size: this.xGridPagerConfig.pageSize,
      sort: this.sort(),
      filter: getFilter(this.searchValue, this.mapOfFilter),
      commonQueryId: this.selectCommonConditionId,
    };
    this.commonTableService()
      .retrieve(paginationQuery)
      .subscribe(
        res => {
          this.xGridData = res.data;
          this.xGridPagerConfig.total = Number(res.headers['x-total-count']);
          this.loading = false;
        },
        err => {
          this.$message.error(err.message);
          this.loading = false;
        }
      );
  }

  public prepareRemove(instance: ICommonTable): void {
    this.removeId = instance.id;
  }

  public removeById(removeId: number): void {
    this.commonTableService()
      .delete(removeId)
      .subscribe((res: AxiosResponse) => {
        const message = this.$t('jhipsterApplicationApp.modelConfigCommonTable.deleted', { param: this.removeId }).toString();
        this.$message.success(message);
        this.loadAll();
      });
  }
  public removeByIds(ids: string[]) {
    this.commonTableService()
      .deleteByIds(ids)
      .subscribe(
        (res: AxiosResponse) => {
          this.$message.success('删除成功');
          this.loadAll();
        },
        err => this.$message.error(err.message)
      );
  }

  public sort(): Array<any> {
    const result = [];
    Object.keys(this.mapOfSort).forEach(key => {
      if (this.mapOfSort[key] && this.mapOfSort[key] !== false) {
        if (this.mapOfSort[key] === 'asc') {
          result.push(key + ',asc');
        } else if (this.mapOfSort[key] === 'desc') {
          result.push(key + ',desc');
        }
      }
    });
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.loadAll();
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public editEntity(row: ICommonTable): void {
    this.$router.push({ path: row.id + '/edit', append: true });
  }

  public designForm(id: number): void {
    this.$router.push({ path: id + '/designer', append: true });
  }

  getCommonTableData() {
    this.commonTableService()
      .findByEntityName('CommonTable')
      .subscribe(res => {
        this.xGridColumns = xGenerateTableColumns(res.data, this.relationshipsData, this.mapOfSort, this.mapOfFilter, this.changeEvent);
        this.treeFilterData = xGenerateFilterTree(res.data, this.relationshipsData);
        if (this.treeFilterData && this.treeFilterData.length > 0) {
          this.filterTreeSpan = 6;
        }
        this.loading = false;
      });
  }

  filterByColumn(fieldName: string, filterValue: string[]) {
    this.mapOfFilter[fieldName].value = filterValue;
    this.loadAll();
  }

  cancelEdit(id: string): void {
    this.loadAll();
  }

  emitEmpty() {
    this.searchInput.focus();
    this.searchValue = '';
  }

  public newEntity(): void {
    this.$router.push({ path: 'new', append: true });
  }

  public copyEntity() {
    if (this.xGridSelectRecords.length === 1) {
      this.$router.push({ path: 'new/' + this.xGridSelectRecords[0].id, append: true });
    }
  }

  public onExpand(expandedKeys) {
    console.log('onExpand', expandedKeys);
    // if not set autoExpandParent to false, if children expanded, parent can not collapse.
    // or, you can remove all expanded children keys.
    this.expandedKeys = expandedKeys;
    this.autoExpandParent = false;
  }
  public onCheck(checkedKeys) {
    console.log('onCheck', checkedKeys);
    this.checkedKeys = checkedKeys;
  }
  public onSelect(selectedKeys, info) {
    console.log('onSelect', info);
    console.log('record', info.node.record);
    const filterData = info.node.dataRef;
    if (filterData.type === 'filterGroup') {
      this.mapOfFilter[info.node.dataRef.key].value = [];
    } else if (filterData.type === 'filterItem') {
      this.mapOfFilter[info.node.dataRef.filterName].value = [info.node.dataRef.filterValue];
    }
    this.loadAll();
    this.selectedKeys = selectedKeys;
  }

  public editClosedEvent({ row, column }) {
    let field = column.property;
    let cellValue = row[field];
    // 判断单元格值是否被修改
    if (this.xGrid.isUpdateByRow(row, field)) {
      const entity = { id: row.id };
      entity[field] = cellValue;
      const options = { 'id.equals': row.id };
      this.commonTableService()
        .updateBySpecifiedField(entity, field, options)
        .subscribe(
          res => {
            if (res.status === 200) {
              this.$message.success({
                content: `信息更新成功。 ${field}=${cellValue}`,
                duration: 1,
              });
              this.xGrid.reloadRow(row, null, field);
            } else {
              this.$message.warning({
                content: `信息保存可能存在问题！ ${field}=${cellValue}`,
                duration: 5,
              });
              this.xGrid.reloadRow(row, null, field);
            }
          },
          error => {
            this.$message.error({
              content: `信息保存可能存在问题！ ${field}=${cellValue}`,
              onClose: () => {},
            });
          }
        );
    }
  }

  public xGridCheckboxChangeEvent() {
    this.xGridSelectRecords = this.xGrid.getCheckboxRecords();
  }
  public changeEvent(e) {
    console.log(e);
  }

  public initRelationships(): void {
    this.loading = true;
    const commonConditionOption = {
      'commonTableClazzName.equals': 'CommonTable',
    };
    forkJoin([
      this.userService().retrieve(),
      this.businessTypeService().retrieve(),
      this.commonConditionService().retrieve(commonConditionOption),
    ]).subscribe(
      ([users, businessTypes, commonConditionsRes]) => {
        this.relationshipsData['users'] = users.data;
        this.relationshipsData['businessTypes'] = businessTypes.data;
        this.commonConditions = commonConditionsRes.data;
        const listOfFiltercreator = users.data.slice(0, users.data.length > 8 ? 7 : users.data.length - 1);
        this.mapOfFilter.creator = { list: listOfFiltercreator, value: [], type: 'many-to-one' };
        const listOfFilterbusinessType = businessTypes.data.slice(0, businessTypes.data.length > 8 ? 7 : businessTypes.data.length - 1);
        this.mapOfFilter.businessType = { list: listOfFilterbusinessType, value: [], type: 'many-to-one' };
        this.getCommonTableData();
      },
      error => {
        this.loading = false;
        this.$message.error({
          content: `数据获取失败`,
          onClose: () => {},
        });
      }
    );
  }

  public xGridPageChange({ currentPage, pageSize }) {
    this.xGridPagerConfig.currentPage = currentPage;
    this.xGridPagerConfig.pageSize = pageSize;
    this.loadAll();
  }

  public switchFilterTree() {
    this.filterTreeSpan = this.filterTreeSpan > 0 ? 0 : 6;
  }

  public xGridSortChange({ property, order }) {
    this.mapOfSort = {};
    this.mapOfSort[property] = order;
    this.loadAll();
  }

  public xGridFilterChange({ column, property, values, datas, filters, $event }) {
    const type = column.params ? column.params.type : '';
    var tempValues;
    if (type === 'STRING') {
      tempValues = datas[0];
    } else if (type === 'INTEGER' || type === 'LONG' || type === 'DOUBLE' || type === 'FLOAT' || type === 'ZONED_DATE_TIME') {
      tempValues = datas[0];
    } else if (type === 'BOOLEAN') {
      tempValues = values;
    }
    this.mapOfFilter[property] = { value: tempValues, type: type };
    this.loadAll();
  }

  public handleConditionMenuClick({ key }) {
    if (key === -1) {
      this.selectCommonConditionId = null;
      this.loadAll();
    } else if (key === 0) {
      this.showCommonConditionModal = true;
    } else {
      this.selectCommonConditionId = key;
      this.loadAll();
    }
  }

  public updateCommonConditionModalCancel() {
    this.showCommonConditionModal = false;
    const commonQueryOption = {
      'commonTableClazzName.equals': 'CommonTable',
    };
    this.commonConditionService()
      .retrieve(commonQueryOption)
      .subscribe(res => {
        this.commonConditions = res.data;
      });
  }
}
