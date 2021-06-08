<script>
import Vue from 'vue';
import VueDraggableResizable from 'vue-draggable-resizable';
import Table from 'ant-design-vue/es/table';
// import Button from "ant-design-vue/es/button";
import Tooltip from 'ant-design-vue/es/tooltip';
import 'ant-design-vue/lib/tooltip/style/css';
import 'ant-design-vue/lib/table/style/css';
import 'ant-design-vue/lib/button/style/css';
import './dataTable.less';
import EditInputs from './extends';
import { getObj } from './util/util';
import cloneDeep from 'lodash/cloneDeep';
Vue.component('vue-draggable-resizable', VueDraggableResizable);
export default {
  name: 'DataTable',
  components: {
    ...EditInputs,
  },
  props: {
    columns: {
      type: Array,
      default() {
        return [];
      },
    },
    mapOfSort: {
      type: Object,
      default() {
        return { id: false };
      },
    },
    mapOfFilter: {
      type: Object,
      default() {
        return {};
      },
    },
    data: {
      type: Array,
      default() {
        return [];
      },
    },
    api: {
      type: Function,
    },
    params: {
      type: Object,
      default() {
        return {};
      },
    },
    dataField: {
      type: String,
      default: 'Data.Datas',
    },
    totalField: {
      type: String,
      default: 'Data.Total',
    },
    pagination: [Object, Boolean],
    rowKey: {
      type: String,
      default: 'Id',
    },
    rowAction: {
      //是否可点击行
      type: Boolean,
      default: false,
    },
    rowSelect: {
      // 是否显示多选或者单选控件
      type: [String, Boolean],
      default: false,
    },
    props: {
      type: Object,
      default() {
        return {};
      },
    },
    autoLoad: {
      type: Boolean,
      required: false,
      default: true,
    },
    eventConfig: {
      type: Object,
      default() {
        return {
          tabToActive: true,
          enterToActive: true,
          rightArrowToActive: true,
          leftArrowToActive: true,
          upArrowToActive: true,
          downArrowToActive: true,
          resizeableColumn: false,
        };
      },
    },
    pageChange: Function,
  },
  watch: {
    params() {
      this.paginationOption.PageIndex = 0;
      this.paginationOption.current = 1;
      this.fetch();
    },
    gridData() {
      this.gridSelectedRow = '';
      this.selectedRowKeys = [];
    },
  },
  data() {
    return {
      paginationOption: {},
      loading: false,
      selectedRowKeys: [],
      gridSelectedRow: '',
      gridData: [],
      originData: [],
    };
  },
  computed: {
    tableScrollX: function () {
      let result = 0;
      this.columns.forEach(column => {
        let columnWidth;
        if (typeof column.width === 'number') {
          columnWidth = column.width;
        } else if (typeof column.width === 'string') {
          columnWidth = Number(column.width.replace('px', ''));
        } else {
          columnWidth = 120;
        }
        result = result + columnWidth;
      });
      return result;
    },
  },
  created() {
    const basePaginationOption = {
      showSizeChanger: true,
      showQuickJumper: true,
      PageSize: 10,
      PageIndex: 0,
      position: 'bottom',
    };
    this.paginationOption = { ...basePaginationOption, ...this.pagination };
    if (this.api && this.autoLoad !== false) {
      this.fetch();
    } else {
      console.log('gridData', this.data);
      this.gridData = cloneDeep(this.data);
    }
  },
  methods: {
    fetch(params = {}) {
      const baseParams = {
        PageSize: this.paginationOption.PageSize,
        PageIndex: this.paginationOption.PageIndex,
      };
      let json = { ...baseParams, ...this.params, ...params };
      if (this.api) {
        this.loading = true;
        this.$emit('beforeLoad', json);
        this.api(json)
          .then(res => {
            const pagination = { ...this.paginationOption };
            const data = getObj(this.dataField, res);
            const total = getObj(this.totalField, res);
            pagination.total = total;
            pagination.current = json.PageIndex + 1;
            this.loading = false;
            this.paginationOption = pagination;
            this.originData = cloneDeep(data);
            this.setData(data);
            this.$emit('afterLoad', data);
          })
          .catch(err => {
            console.log(err);
            this.loading = false;
          });
      }
    },
    //设置表格数据
    setData(data) {
      this.gridData = [];
      this.gridData = cloneDeep(data);
    },
    //获取表格数据
    getData() {
      return this.gridData;
    },
    //设置当前选中行
    setSelected(row) {
      this.gridSelectedRow = row;
    },
    //设置多选选中行数据
    setSelecteds(keys) {
      this.selectedRowKeys = keys;
    },
    //获取选中行
    getSelected() {
      return this.gridSelectedRow;
    },
    //获取多选中行
    getSelecteds() {
      return this.selectedRows;
    },
    reload(params = {}) {
      this.fetch(params);
    },
    // 翻页
    handleTableChange(pagination, filters, sorter) {
      console.log('sorter', sorter);
      const pager = { ...this.paginationOption };
      pager.PageIndex = pagination.current - 1;
      pager.current = pagination.current;
      pager.PageSize = pagination.pageSize;
      pager.pageSize = pagination.pageSize;
      this.paginationOption = pager;
      let json = {
        PageSize: pager.PageSize,
        PageIndex: pager.PageIndex,
        ...filters,
      };
      this.fetch(json);
    },
    //无单选或多选控件时选择行事件
    onSelectedRow(row) {
      this.$emit('beforeselect', this.gridSelectedRow);
      if (this.gridSelectedRow[this.rowKey] === row[this.rowKey]) {
        this.gridSelectedRow = '';
      } else {
        this.gridSelectedRow = row;
      }
      this.$emit('select', this.gridSelectedRow);
      this.$emit('afterselect', this.gridSelectedRow[this.rowKey], this.gridSelectedRow);
    },
    isChange() {
      let change = false;
      for (let i = 0; i < this.originData.length; i++) {
        const el = this.originData[i];
        const compareObj = this.gridData[i];
        for (let key in el) {
          if (typeof el[key] != 'object' && el[key] != compareObj[key]) {
            change = true;
            break;
          }
        }
      }
      return change;
    },
    // 单选或多选控件选择时触发事件
    onSelectGridChange(selectedRowKeys, selectedRows) {
      this.$emit('beforeselect', this.selectedRows);
      this.selectedRowKeys = selectedRowKeys;
      console.log(selectedRows);
      this.$emit('afterselect', selectedRowKeys, selectedRows);
    },
    updateCell(data, index, key) {
      const newData = [...this.gridData];
      newData[index][key] = data;
      this.setData(newData);
    },
    onCellEnter(index, refIndex, columnLen) {
      console.log('onCellEnter');
      let roRowIndex = index,
        toColumnIndex = refIndex + 1;
      if (refIndex == columnLen - 1) {
        roRowIndex = index + 1;
        toColumnIndex = 0;
      }
      const refName = 'edit_' + roRowIndex + '_' + toColumnIndex;
      let nextInput = this.$refs[refName];
      if (nextInput) {
        if (nextInput.$refs.EditSwitch || nextInput.$refs.EditCheckbox) {
          this.onCellEnter(roRowIndex, toColumnIndex, columnLen);
        } else {
          nextInput.check();
        }
      } else {
        if (roRowIndex == this.gridData.length && refIndex == columnLen - 1) {
          return;
        } else {
          this.onCellEnter(roRowIndex, toColumnIndex, columnLen);
        }
      }
    },
    onBackCellEnter(index, refIndex, columnLen) {
      console.log('onBackCellEnter');
      let roRowIndex = index,
        toColumnIndex = refIndex - 1;
      if (refIndex == 0) {
        roRowIndex = index - 1;
        toColumnIndex = columnLen - 1;
      }
      const refName = 'edit_' + roRowIndex + '_' + toColumnIndex;
      let nextInput = this.$refs[refName];
      if (nextInput) {
        if (nextInput.$refs.EditSwitch || nextInput.$refs.EditCheckbox) {
          this.onBackCellEnter(roRowIndex, toColumnIndex, columnLen);
        } else {
          nextInput.check();
        }
      } else {
        if (roRowIndex == 0 && toColumnIndex == 0) {
          return;
        }
        this.onBackCellEnter(index - 1, columnLen, columnLen);
      }
    },
    onUpDownCellEnter(index, refIndex, type) {
      console.log('onUpDownCellEnter');
      if ((index == 0 && type == 'up') || (index == this.gridData.length - 1 && type == 'down')) {
        return;
      }
      const roRowIndex = type == 'up' ? index - 1 : index + 1;

      const refName = 'edit_' + roRowIndex + '_' + refIndex;
      let nextInput = this.$refs[refName];
      if (nextInput) {
        nextInput.check();
      }
    },
    getGridContent(h) {
      let editScopedSlots = { ...this.$scopedSlots };
      let inputIndex = 0;
      const _this = this;
      const draggingMap = {};
      const columns = this.columns.map((item, columnIndex) => {
        if (item.type === 'indexcolumn') {
          //索引列
          item.customRender = (text, record, index) => `${index + 1}`;
        }
        item.align = item.align ? item.align : 'left';
        if (item.edit) {
          const inputs = {
            input: 'EditInput',
            select: 'EditSelect',
            number: 'EditNumber',
            date: 'EditDate',
            checkbox: 'EditCheckbox',
            switch: 'EditSwitch',
            textarea: 'EditTextarea',
          };
          const slotName = 'edit_' + item.dataIndex;
          const columnTemplate = (text, record, index) => {
            const refName = 'edit_' + index + '_' + columnIndex;
            const _index = inputIndex;
            const nodeProps = item.props ? item.props : {};

            const props = {
              ref: refName,
              props: {
                ...nodeProps,
                inputIndex: _index,
                columnIndex: columnIndex,
                record: record,
                index: index,
                column: item,
                data: text,
                eventConfig: this.eventConfig,
              },
              style: item.style ? item.style : {},
              class: 'edit_' + index + '_' + columnIndex,
              on: {
                change: data => {
                  _this.updateCell(data, index, item.dataIndex);
                  if (item.onChange) {
                    item.onChange(data, record);
                  }
                },
                onEnter: () => {
                  _this.onCellEnter(index, columnIndex, this.columns.length);
                },
                onBackEnter: () => {
                  _this.onBackCellEnter(index, columnIndex, this.columns.length);
                },
                onUpEnter: () => {
                  _this.onUpDownCellEnter(index, columnIndex, 'up');
                },
                onDownEnter: () => {
                  _this.onUpDownCellEnter(index, columnIndex, 'down');
                },
                input: function (event) {
                  _this.$emit('input', event.target.value);
                },
              },
            };
            const type = item.edit.type ? item.edit.type : 'input';
            const inputName = inputs[type];
            inputIndex++;
            return h(inputName, props);
          };

          editScopedSlots[slotName] = columnTemplate;
          item.scopedSlots = { customRender: slotName }; //构建编辑域插槽
        } else if (item.tooltip) {
          item.customRender = (text, record, index) => {
            console.log(record, index);
            let width = typeof item.width === 'number' ? item.width + 'px' : item.width;
            if (!width) {
              width = '100px';
            }
            return (
              <Tooltip>
                <template slot="title">{text}</template>
                <span class="table-cell-text" style={'max-width:' + width}>
                  {text}
                </span>
              </Tooltip>
            );
          };
        }
        item.key = item.dataIndex;
        item.sortOrder = this.mapOfSort[item.dataIndex];
        if (this.mapOfFilter[item.dataIndex]) {
          item.filters = this.mapOfFilter[item.dataIndex].list;
          item.filteredValue = this.mapOfFilter[item.dataIndex].value;
        }
        draggingMap[item.key] = item.width;
        return item;
      });
      const draggingState = Vue.observable(draggingMap);
      const ResizeableTitle = rh => {
        let thDom = null;
        const { key, ...restProps } = rh.props;
        let col = columns.find(col => {
          const k = col.dataIndex || col.key;
          return k === key;
        });
        if (key == 'selection-column') {
          col = {
            dataIndex: 'selection-column',
            width: 20,
            align: 'center',
          };
        }
        if (!col.width) {
          return <th {...restProps}>{rh.children}</th>;
        }
        const onDrag = x => {
          draggingState[key] = 0;
          col.width = Math.max(x, 1);
        };

        const onDragstop = () => {
          draggingState[key] = thDom.getBoundingClientRect().width;
        };
        return (
          <th {...restProps} v-ant-ref={r => (thDom = r)} width={col.width} data-key={String(col.key)} class="resize-table-th">
            {rh.children}
            <vue-draggable-resizable
              key={col.key}
              class="table-draggable-handle"
              w={10}
              x={draggingState[key] || col.width}
              z={1}
              axis="x"
              draggable={true}
              resizable={false}
              onDragging={onDrag}
              onDragstop={onDragstop}
            ></vue-draggable-resizable>
          </th>
        );
      };
      const props = {
        ...this.props,
        columns: columns,
        dataSource: this.gridData,
        rowKey: this.rowKey,
        pagination: this.paginationOption,
        loading: this.loading,
        scroll: { x: this.tableScrollX },
        customRow: record => {
          return {
            on: {
              // 点击行
              click: e => {
                this.$emit('rowclick', e);
                if (!_this.rowSelect && this.rowAction) {
                  _this.onSelectedRow(record);
                }
              },
              dblclick: e => {
                this.$emit('rowdblclick', e);
              },
            },
          };
        },
        rowClassName: record => {
          if (this.gridSelectedRow[this.rowKey] && record[this.rowKey] === this.gridSelectedRow[this.rowKey]) {
            return 'active';
          } else {
            return '';
          }
        },
      };
      if (this.eventConfig.resizeableColumn) {
        props.components = {
          header: {
            cell: ResizeableTitle,
          },
        };
      }
      //是否显示多选或者单选控件
      if (this.rowSelect) {
        props.rowSelection = {
          selectedRowKeys: this.selectedRowKeys,
          type: this.rowSelect === 'radio' ? 'radio' : 'checkbox',
          onChange: (selectedRowKeys, selectedRows) => {
            this.onSelectGridChange(selectedRowKeys, selectedRows);
          },
          onSelect: (record, selected, selectedRows) => {
            this.$emit('rowclick', selected);
            this.selectedRows = selectedRows;
            this.$emit('select', record);
          },
          onSelectAll: (selected, selectedRows) => {
            this.selectedRows = selectedRows;
          },
        };
      }
      if (this.pagination === false || !this.gridData || this.gridData.length < 1) {
        props.pagination = false;
      }
      const dataGridProps = {
        ref: 'dataGrid',
        props: props,
        on: {
          change(pagination, filters, sorter) {
            _this.$emit('change', pagination, filters, sorter);
            // _this.handleTableChange(pagination, filters, sorter);
          },
        },
        scopedSlots: editScopedSlots,
      };
      return dataGridProps;
    },
  },
  render(h) {
    const dataGridProps = this.getGridContent(h);
    return (
      <div class="data-grid">
        <Table {...dataGridProps}></Table>
        <div class={'data-grid-footer ' + this.paginationOption.position}>{this.$slots['footerAction']}</div>
      </div>
    );
  },
};
</script>
