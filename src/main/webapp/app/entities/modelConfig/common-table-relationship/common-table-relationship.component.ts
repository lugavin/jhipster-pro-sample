import { mixins } from 'vue-class-component';
import { Component, Inject, Prop, Vue, Ref, Watch } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICommonTableRelationship, CommonTableRelationship } from '@/shared/model/modelConfig/common-table-relationship.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import CommonTableRelationshipService from './common-table-relationship.service';
import { ICommonTable } from '@/shared/model/modelConfig/common-table.model';
import CommonTableService from '@/entities/modelConfig/common-table/common-table.service';
import { IDataDictionary } from '@/shared/model/settings/data-dictionary.model';
import DataDictionaryService from '@/entities/settings/data-dictionary/data-dictionary.service';
import { AxiosResponse } from 'axios';
import { getFilter, xGenerateFilterTree, xGenerateTableColumns } from '@/utils/entity-list-utils';
import CommonTableRelationshipUpdate from '@/entities/modelConfig/common-table-relationship/common-table-relationship-update.vue';
import { forkJoin } from 'rxjs';

@Component
export default class CommonTableRelationshipComponent extends Vue {
  @Inject('commonTableRelationshipService') private commonTableRelationshipService: () => CommonTableRelationshipService;
  @Inject('commonTableService') private commonTableService: () => CommonTableService;
  @Inject('dataDictionaryService') private dataDictionaryService: () => DataDictionaryService;
  @Ref('searchInput') public searchInput;
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
  public xGridTreeConfig: boolean | Object = false;
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
  public omitFields = ['id'];
  public commonTableRelationships: ICommonTableRelationship[] = [];
  public mapOfFilter: { [key: string]: any } = {
    relationEntity: { list: [], value: [], type: 'many-to-one' },
    dataDictionaryNode: { list: [], value: [], type: 'many-to-one' },
    metaModel: { list: [], value: [], type: 'many-to-one' },
    commonTable: { list: [], value: [], type: 'many-to-one' },
  };
  public editStatus: { [key: string]: any } = {};
  public isFetching = false;
  @Prop(Boolean) showInOther;
  public editInModal = false;
  public updateModalVisible: boolean = false;
  public commonTableRelationshipId = null;
  public clickCommonTableRelationshipId;
  public searchValue = '';
  commontables: ICommonTable[];
  datadictionaries: IDataDictionary[];
  @Prop(Number) commonTableId: number;

  public created(): void {
    this.initRelationships();
    if (this.commonTableId) {
      this.mapOfFilter['commonTable'] = { list: [], value: [this.commonTableId], type: 'many-to-one' };
    }
  }

  public mounted(): void {
    this.loadAll();
  }

  public clear(): void {
    this.xGridPagerConfig.currentPage = 1;
    this.loadAll();
  }
  @Watch('commonTableId')
  public loadAll(): void {
    if (this.showInOther && !this.commonTableId) {
      return;
    }
    if (this.commonTableId) {
      this.mapOfFilter['commonTable'] = { list: [], value: [this.commonTableId], type: 'many-to-one' };
    }
    this.loading = true;

    const paginationQuery = {
      listModelName: 'CommonTableRelationship',
      page: this.xGridPagerConfig.currentPage - 1,
      size: this.xGridPagerConfig.pageSize,
      sort: this.sort(),
      filter: getFilter(this.searchValue, this.mapOfFilter),
    };
    this.commonTableRelationshipService()
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

  public prepareRemove(instance: ICommonTableRelationship): void {
    this.removeId = instance.id;
  }

  public removeById(removeId: number): void {
    this.commonTableRelationshipService()
      .delete(removeId)
      .subscribe((res: AxiosResponse) => {
        const message = this.$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.deleted', { param: this.removeId }).toString();
        this.$message.success(message);
        this.loadAll();
      });
  }
  public removeByIds(ids: string[]) {
    this.commonTableRelationshipService()
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

  public editEntity(row: ICommonTableRelationship): void {
    if (this.showInOther || this.editInModal) {
      this.commonTableRelationshipId = row.id;
      this.updateModalVisible = true;
    } else {
      this.$router.push({ path: row.id + '/edit', append: true });
    }
  }

  getCommonTableData() {
    this.commonTableService()
      .findByEntityName('CommonTableRelationship')
      .subscribe(res => {
        this.editInModal = res.data.editInModal;
        this.xGridColumns = xGenerateTableColumns(res.data, this.relationshipsData, this.mapOfSort, this.mapOfFilter, this.changeEvent);
        this.treeFilterData = xGenerateFilterTree(res.data, this.relationshipsData);
        if (res.data.treeTable) {
          this.xGridTreeConfig = { children: 'children', indent: 20, line: false, expandAll: false, accordion: false, trigger: 'default' };
        }
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

  getFilterTest() {
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
    if (this.commonTableId) {
      result['commonTableId.in'] = this.commonTableId;
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
    this.commonTableRelationshipId = null;
    this.updateModalVisible = false;
    this.loadAll();
  }

  public newEntity(): void {
    if (this.showInOther || this.editInModal) {
      this.updateModalVisible = true;
    } else {
      this.$router.push({ path: 'new', append: true });
    }
  }

  public editClosedEvent({ row, column }) {
    let field = column.property;
    let cellValue = row[field];
    // 判断单元格值是否被修改
    if (this.xGrid.isUpdateByRow(row, field)) {
      const entity = { id: row.id };
      entity[field] = cellValue;
      this.commonTableRelationshipService()
        .updateBySpecifiedField(entity, field)
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
    forkJoin([this.commonTableService().retrieve(), this.dataDictionaryService().tree()]).subscribe(
      ([commonTables, dataDictionaries]) => {
        this.relationshipsData['commonTables'] = commonTables.data;
        this.relationshipsData['dataDictionaries'] = dataDictionaries.data;
        const listOfFilterrelationEntity = commonTables.data.slice(0, commonTables.data.length > 8 ? 7 : commonTables.data.length - 1);
        this.mapOfFilter.relationEntity = { list: listOfFilterrelationEntity, value: [], type: 'many-to-one' };
        const listOfFilterdataDictionaryNode = dataDictionaries.data.slice(
          0,
          dataDictionaries.data.length > 8 ? 7 : dataDictionaries.data.length - 1
        );
        this.mapOfFilter.dataDictionaryNode = { list: listOfFilterdataDictionaryNode, value: [], type: 'many-to-one' };
        const listOfFiltermetaModel = commonTables.data.slice(0, commonTables.data.length > 8 ? 7 : commonTables.data.length - 1);
        this.mapOfFilter.metaModel = { list: listOfFiltermetaModel, value: [], type: 'many-to-one' };
        const listOfFiltercommonTable = commonTables.data.slice(0, commonTables.data.length > 8 ? 7 : commonTables.data.length - 1);
        this.mapOfFilter.commonTable = { list: listOfFiltercommonTable, value: [], type: 'many-to-one' };
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

  public switchFilterTree() {
    this.filterTreeSpan = this.filterTreeSpan > 0 ? 0 : 6;
  }
}
