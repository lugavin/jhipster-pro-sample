import { mixins } from 'vue-class-component';
import { Component, Inject, Prop, Ref, Vue, Watch } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICommonConditionItem, CommonConditionItem } from '@/shared/model/report/common-condition-item.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import CommonConditionItemService from './common-condition-item.service';
import { ICommonCondition } from '@/shared/model/report/common-condition.model';
import CommonConditionService from '@/entities/report/common-condition/common-condition.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import { AxiosResponse } from 'axios';
import { xGenerateTableColumns, xGenerateFilterTree } from '@/utils/entity-list-utils';
import CommonConditionItemUpdate from '@/entities/report/common-condition-item/common-condition-item-update.vue';
import { forkJoin } from 'rxjs';

@Component({
  components: {
    'common-condition-item-update': CommonConditionItemUpdate,
  },
})
export default class CommonConditionItemCompactComponent extends Vue {
  @Inject('commonConditionItemService') private commonConditionItemService: () => CommonConditionItemService;
  @Inject('commonConditionService') private commonConditionService: () => CommonConditionService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  @Ref() public searchInput;
  @Ref('xGridCompact') public xGridCompact;
  public xGridData = [];
  public xGridColumns = [];
  public xGridTableToolbars = {
    perfect: true,
    zoom: true,
    custom: true,
    slots: {
      buttons: 'toolbar_buttons',
    },
  };
  public xGridCheckboxConfig = {
    labelField: 'id',
  };
  public xGridRadioConfig = {
    labelField: 'id',
  };
  public xGridRadioSelectRecord = null;
  public xGridSelectRecords = [];
  private loading: boolean = false;
  public relationshipsData: any = {};
  public xGridPagerConfig = {
    layouts: ['PrevPage', 'NextPage', 'Sizes', 'Total'],
    pageSize: 15,
    pageSizes: [5, 10, 15, 20, 30, 50],
    total: 0,
    pagerCount: 5,
    currentPage: 1,
  };

  @Prop(Boolean) showInOther;

  // @Ref('dataGrid') public dataGrid
  private removeId: number = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public filterTreeSpan = 0;
  public dataTableColumns = [];
  public dataTableParams = {
    Status: 1,
    PageSize: 5,
  };
  public dataTableProps = {
    bordered: true,
  };
  public dataTableEventConfig = {
    tabToActive: true, //Tab键进入下一个单元格编辑
    enterToActive: true, //回车键进入下一个单元格编辑
    rightArrowToActive: true, //向右箭头激活同一行下一列的编辑
    leftArrowToActive: true, //向左箭头激活同一行前一列的编辑
    upArrowToActive: true, //向上箭头激活上一行相同列的编辑
    downArrowToActive: true, //向下箭头激活下一行相同列的编辑
    resizeableColumn: false, //列宽拖拽调整,设为true的话，column必须设宽度值，且为整数，如：width:100
  };
  @Prop(Object) otherPresetOrder: { [key: string]: any };
  @Prop() rowIdName;
  @Prop(String) commonTableName;
  @Prop() selectIds;
  @Prop() selectModel;
  public treeFilterData = [];
  public expandedKeys = [];
  public autoExpandParent = true;
  public checkedKeys = [];
  public selectedKeys = [];
  public mapOfSort: { [key: string]: any } = {};
  public reverse = false;
  public totalItems = 0;
  public omitFields = ['id'];
  public commonConditionItems: ICommonConditionItem[] = [];
  public commonConditionItemCommonTableData: ICommonTable = null; // 实体模型数据
  public mapOfFilter: { [key: string]: any } = {
    commonCondition: { list: [], value: [], type: 'many-to-one' },
  };
  public editStatus: { [key: string]: any } = {};
  public isFetching = false;
  public updateModalVisible: boolean = false;
  public commonConditionItemId = null;
  public searchValue = '';
  @Prop(Number) commonConditionId: number;
  commonconditions: ICommonCondition[];

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
  @Watch('commonConditionId')
  public loadAll(): void {
    if (this.showInOther && !this.commonConditionId) {
      return;
    }
    this.loading = true;

    const paginationQuery = {
      page: this.xGridPagerConfig.currentPage - 1,
      size: this.xGridPagerConfig.pageSize,
      sort: this.sort(),
      filter: this.getFilter(),
    };
    this.commonConditionItemService()
      .retrieve(paginationQuery)
      .subscribe(
        res => {
          this.xGridData = res.data;
          this.xGridPagerConfig.total = Number(res.headers['x-total-count']);
          if (this.xGridSelectType === 'checkbox') {
            if (Array.isArray(this.selectIds) && this.selectIds.length > 0) {
              this.$nextTick(() => {
                const rows = [];
                this.selectIds.forEach(id => {
                  rows.push(this.xGridCompact.getRowById(id));
                });
                this.xGridCompact.setCheckboxRow(rows, true);
                this.xGridSelectRecords = this.xGridCompact.getCheckboxRecords();
              });
            }
          } else {
            if (this.selectIds) {
              this.$nextTick(() => {
                this.xGridCompact.setRadioRow(this.xGridCompact.getRowById(this.selectIds));
                this.xGridRadioSelectRecord = this.xGridCompact.getRadioRecord();
              });
            }
          }
          this.loading = false;
        },
        err => {
          this.$message.error(err.message);
          this.loading = false;
        }
      );
  }

  public prepareRemove(instance: ICommonConditionItem): void {
    this.removeId = instance.id;
  }

  public removeById(removeId: number): void {
    this.commonConditionItemService()
      .delete(removeId)
      .subscribe((res: AxiosResponse) => {
        const message = this.$t('jhipsterApplicationApp.reportCommonConditionItem.deleted', { param: this.removeId }).toString();
        this.$message.success(message);
        this.loadAll();
      });
  }
  public removeByIds(ids: string[]) {
    this.commonConditionItemService()
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
    if (this.showInOther && this.otherPresetOrder) {
      Object.keys(this.otherPresetOrder).forEach(key => {
        if (this.otherPresetOrder[key] && this.otherPresetOrder[key] !== false) {
          if (this.otherPresetOrder[key] === 'asc') {
            result.push(key + ',asc');
          } else if (this.otherPresetOrder[key] === 'desc') {
            result.push(key + ',desc');
          }
        }
      });
      return result;
    }
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

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  handleTableChange(pagination, filters, sorter) {
    if (sorter && sorter.columnKey) {
      this.propOrder = sorter.columnKey;
      this.reverse = sorter.order === 'ascend';
    } else {
      this.propOrder = 'id';
      this.reverse = false;
    }
    Object.keys(filters).forEach(key => {
      this.mapOfFilter[key].value = filters[key];
    });
    this.page = pagination.current;
    this.loadAll();
  }

  getCommonTableData() {
    this.loading = true;
    this.commonTableService()
      .findByEntityName('CommonConditionItem')
      .subscribe(
        res => {
          this.xGridColumns = xGenerateTableColumns(
            res.data,
            this.relationshipsData,
            this.mapOfSort,
            this.mapOfFilter,
            this.changeEvent,
            true
          );
          if (this.selectModel !== 'multiple') {
            this.xGridColumns.find(column => {
              return column.type === 'checkbox' && column.width === 60;
            }).type = 'radio';
          }
          this.treeFilterData = xGenerateFilterTree(res.data, this.relationshipsData);
          if (this.treeFilterData && this.treeFilterData.length > 0) {
            this.filterTreeSpan = 6;
          }
          this.loading = false;
        },
        error => {
          this.loading = false;
          this.$message.error(error.message);
        }
      );
  }

  filterByColumn(fieldName: string, filterValue: string[]) {
    this.mapOfFilter[fieldName].value = filterValue;
    this.loadAll();
  }
  getFilter() {
    const result: { [key: string]: any } = {};
    if (this.searchValue) {
      result['jhiCommonSearchKeywords'] = this.searchValue;
      return result;
    }
    Object.keys(this.mapOfFilter).forEach(key => {
      const filterResult = [];
      if (this.mapOfFilter[key].type === 'Enum') {
        this.mapOfFilter[key].value.forEach(value => {
          filterResult.push(value);
        });
        result[key + '.in'] = filterResult;
      }
      if (['one-to-one', 'many-to-many', 'many-to-one', 'one-to-many'].includes(this.mapOfFilter[key].type)) {
        this.mapOfFilter[key].value.forEach(value => {
          filterResult.push(value);
        });
        result[key + 'Id.in'] = filterResult;
      }
    });
    if (this.commonConditionId) {
      result['commonConditionId.in'] = this.commonConditionId;
    }
    return result;
  }

  cancelEdit(id: string): void {
    this.loadAll();
  }

  emitEmpty() {
    this.searchInput.focus();
    this.searchValue = '';
  }

  public updateModalCancel(e) {
    this.updateModalVisible = false;
    this.loadAll();
  }

  public newEntity(): void {
    if (this.showInOther) {
      this.updateModalVisible = true;
    } else {
      this.$router.push({ path: 'new', append: true });
    }
  }

  public initRelationships(): void {
    this.loading = true;
    forkJoin([this.commonConditionService().retrieve()]).subscribe(
      ([commonConditions]) => {
        this.relationshipsData['commonConditions'] = commonConditions.data;
        const listOfFiltercommonCondition = commonConditions.data.slice(
          0,
          commonConditions.data.length > 8 ? 7 : commonConditions.data.length - 1
        );
        this.mapOfFilter.commonCondition = { list: listOfFiltercommonCondition, value: [], type: 'many-to-one' };
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

  public changeEvent(e) {
    console.log(e);
  }

  public xGridPageChange({ currentPage, pageSize }) {
    this.xGridPagerConfig.currentPage = currentPage;
    this.xGridPagerConfig.pageSize = pageSize;
    this.loadAll();
  }

  public handleCancel() {
    this.$emit('cancel');
  }

  public handleOK() {
    if (this.xGridSelectType === 'checkbox') {
      this.$emit('ok', this.xGridSelectRecords);
    } else {
      this.$emit('ok', this.xGridRadioSelectRecord);
    }
  }

  public xGridCheckboxChangeEvent() {
    this.xGridSelectRecords = this.xGridCompact.getCheckboxRecords();
  }

  get xGridSelectType() {
    if (this.selectModel !== 'multiple') {
      return 'radio';
    } else {
      return 'checkbox';
    }
  }

  public xGridRadioChangeEvent() {
    this.xGridRadioSelectRecord = this.xGridCompact.getRadioRecord();
  }
}
