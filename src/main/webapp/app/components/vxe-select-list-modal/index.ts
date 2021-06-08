import { CreateElement } from 'vue';
import {
  ColumnCellRenderOptions,
  ColumnCellRenderParams,
  ColumnEditRenderOptions,
  ColumnEditRenderParams,
  ColumnExportCellRenderParams,
  ColumnFilterParams,
  ColumnFilterRenderParams,
  FormItemRenderOptions,
  FormItemRenderParams,
  RenderOptions,
  RenderParams,
  TableRenderParams,
  VXETable,
} from 'vxe-table/lib/vxe-table';
import XEUtils from 'xe-utils/methods/xe-utils';
import moment from 'moment';

function getModelEvent(renderOpts: RenderOptions) {
  let type = 'change';
  switch (renderOpts.name) {
    case 'AInput':
      type = 'change.value';
      break;
    case 'ARadio':
    case 'ACheckbox':
      type = 'input';
      break;
  }
  return type;
}

function getChangeEvent(renderOpts: RenderOptions) {
  return 'change';
}

function getCellEditFilterProps(renderOpts: RenderOptions, params: TableRenderParams, value: any, defaultProps?: { [prop: string]: any }) {
  const { vSize } = params.$table;
  return XEUtils.assign(vSize ? { size: vSize } : {}, defaultProps, renderOpts.props, { [getModelProp(renderOpts)]: value });
}

function getModelProp(renderOpts: RenderOptions) {
  let prop = 'value';
  switch (renderOpts.name) {
    case 'ASwitch':
      prop = 'checked';
      break;
  }
  return prop;
}

function getOns(renderOpts: RenderOptions, params: RenderParams, inputFunc?: Function, changeFunc?: Function) {
  const { events } = renderOpts;
  const modelEvent = getModelEvent(renderOpts);
  const changeEvent = getChangeEvent(renderOpts);
  const isSameEvent = changeEvent === modelEvent;
  const ons: { [type: string]: Function } = {};
  XEUtils.objectEach(events, (func: Function, key: string) => {
    ons[key] = function (...args: any[]) {
      func(params, ...args);
    };
  });
  if (inputFunc) {
    ons[modelEvent] = function (targetEvnt: any) {
      inputFunc(targetEvnt);
      if (events && events[modelEvent]) {
        events[modelEvent](params, targetEvnt);
      }
      if (isSameEvent && changeFunc) {
        changeFunc(targetEvnt);
      }
    };
  }
  if (!isSameEvent && changeFunc) {
    ons[changeEvent] = function (...args: any[]) {
      changeFunc(...args);
      if (events && events[changeEvent]) {
        events[changeEvent](params, ...args);
      }
    };
  }
  return ons;
}

function cellText(h: CreateElement, cellValue: any) {
  return ['' + (isEmptyValue(cellValue) ? '' : cellValue)];
}

function isEmptyValue(cellValue: any) {
  return cellValue === null || cellValue === undefined || cellValue === '';
}

function getEditOns(renderOpts: RenderOptions, params: ColumnEditRenderParams) {
  const { $table, row, column } = params;
  return getOns(
    renderOpts,
    params,
    (value: any) => {
      // 处理 model 值双向绑定
      XEUtils.set(row, column.property, value);
    },
    () => {
      // 处理 change 事件相关逻辑
      $table.updateStatus(params);
    }
  );
}

function getSelectListModalCellValue(renderOpts: ColumnCellRenderOptions, params: ColumnCellRenderParams) {
  const { options = [], optionGroups, props = {}, optionProps = {}, optionGroupProps = {} } = renderOpts;
  const { row, column } = params;
  const labelProp = optionProps.label || 'label';
  const valueProp = optionProps.value || 'value';
  const groupOptions = optionGroupProps.options || 'options';
  const cellValue = XEUtils.get(row, column.property);
  if (!isEmptyValue(cellValue)) {
    return XEUtils.map(
      props.mode === 'multiple' ? cellValue : [cellValue],
      optionGroups
        ? value => {
            let selectItem;
            for (let index = 0; index < optionGroups.length; index++) {
              selectItem = XEUtils.find(optionGroups[index][groupOptions], item => item[valueProp] === value);
              if (selectItem) {
                break;
              }
            }
            return selectItem ? selectItem[labelProp] : value;
          }
        : value => {
            const selectItem = XEUtils.find(options, item => item[valueProp] === value);
            return selectItem ? selectItem[labelProp] : value;
          }
    ).join(', ');
  }
  return null;
}

function getFilterOns(renderOpts: RenderOptions, params: ColumnFilterRenderParams, option: ColumnFilterParams, changeFunc: Function) {
  return getOns(
    renderOpts,
    params,
    (value: any) => {
      // 处理 model 值双向绑定
      option.data = value;
    },
    changeFunc
  );
}

function handleConfirmFilter(params: ColumnFilterRenderParams, checked: boolean, option: ColumnFilterParams) {
  const { $panel } = params;
  $panel.changeOption({}, checked, option);
}

function getItemProps(renderOpts: RenderOptions, params: FormItemRenderParams, value: any, defaultProps?: { [prop: string]: any }) {
  const { vSize } = params.$form;
  return XEUtils.assign(vSize ? { size: vSize } : {}, defaultProps, renderOpts.props, { [getModelProp(renderOpts)]: value });
}

function getItemOns(renderOpts: RenderOptions, params: FormItemRenderParams) {
  const { $form, data, property } = params;
  return getOns(
    renderOpts,
    params,
    (value: any) => {
      // 处理 model 值双向绑定
      XEUtils.set(data, property, value);
    },
    () => {
      // 处理 change 事件相关逻辑
      $form.updateStatus(params);
    }
  );
}

function createExportMethod(valueMethod: Function, isEdit?: boolean) {
  const renderProperty = isEdit ? 'editRender' : 'cellRender';
  return function (params: ColumnExportCellRenderParams) {
    return valueMethod(params.column[renderProperty], params);
  };
}

const renderMap = {
  ASelectListModal: {
    renderEdit(h: CreateElement, renderOpts: ColumnEditRenderOptions, params: ColumnEditRenderParams) {
      const { options = [], optionProps = {} } = renderOpts;
      const { row, column } = params;
      const { attrs } = renderOpts;
      const cellValue = XEUtils.get(row, column.property);
      const props = getCellEditFilterProps(renderOpts, params, cellValue);
      props.options = options;
      props.optionProps = optionProps;
      const on = getEditOns(renderOpts, params);
      return [
        h('select-list-modal', {
          props,
          attrs,
          on,
        }),
      ];
    },
    renderCell(h: CreateElement, renderOpts: ColumnCellRenderOptions, params: ColumnCellRenderParams) {
      return cellText(h, getSelectListModalCellValue(renderOpts, params));
    },
    renderItem(h: CreateElement, renderOpts: FormItemRenderOptions, params: FormItemRenderParams) {
      const { options = [], optionGroups, optionProps = {}, optionGroupProps = {} } = renderOpts;
      const { data, property } = params;
      const { attrs } = renderOpts;
      const itemValue = XEUtils.get(data, property);
      const props = getItemProps(renderOpts, params, itemValue);
      props.options = options;
      props.optionProps = optionProps;
      const on = getItemOns(renderOpts, params);
      return [
        h('select-list-modal', {
          attrs,
          props,
          on,
        }),
      ];
    },
    cellExportMethod: createExportMethod(getSelectListModalCellValue),
    editCellExportMethod: createExportMethod(getSelectListModalCellValue, true),
  },
  ASlider: {
    renderEdit: createEditRender(),
    renderDefault: createEditRender(),
    renderFilter: function renderFilter(h, renderOpts, params) {
      var column = params.column;
      var attrs = renderOpts.attrs;
      return [
        h(
          'div',
          {
            class: 'vxe-table--filter-iview-wrapper',
          },
          column.filters.map(function (option, oIndex) {
            var optionValue = option.data;
            var props = getCellEditFilterProps(renderOpts, params, optionValue);
            return h('a-slider', {
              attrs: attrs,
              props: props,
              on: getFilterOns(renderOpts, params, option, function () {
                // 处理 change 事件相关逻辑
                handleConfirmFilter(params, option.data && option.data.length > 0, option);
              }),
            });
          })
        ),
      ];
    },
  },
  AARangePicker: {
    renderFilter: function renderFilter(h, renderOpts, params) {
      var column = params.column;
      var attrs = renderOpts.attrs;
      return [
        h(
          'div',
          {
            class: 'vxe-table--filter-iview-wrapper',
          },
          column.filters.map(function (option, oIndex) {
            var optionValue = option.data;
            var props = getCellEditFilterProps(renderOpts, params, optionValue);
            return h('a-range-picker', {
              attrs: attrs,
              props: props,
              on: getFilterOns(renderOpts, params, option, function () {
                // 处理 change 事件相关逻辑
                handleConfirmFilter(params, option.data && option.data.length > 0, option);
              }),
            });
          })
        ),
      ];
    },
  },
};
function createEditRender(defaultProps?) {
  return function (h, renderOpts, params) {
    var row = params.row,
      column = params.column;
    var attrs = renderOpts.attrs;

    var cellValue = XEUtils['default'].get(row, column.property);

    return [
      h(renderOpts.name, {
        attrs: attrs,
        props: getCellEditFilterProps(renderOpts, params, cellValue, defaultProps),
        on: getEditOns(renderOpts, params),
      }),
    ];
  };
}

function createASlideFilterRender(defaultProps?) {
  return function (h, renderOpts, params) {
    var column = params.column;
    var name = renderOpts.name,
      attrs = renderOpts.attrs;
    return [
      h(
        'div',
        {
          class: 'vxe-table--filter-iview-wrapper',
        },
        column.filters.map(function (option, oIndex) {
          var optionValue = option.data;
          return h(name, {
            key: oIndex,
            attrs: attrs,
            props: getCellEditFilterProps(renderOpts, params, optionValue, defaultProps),
            on: getFilterOns(renderOpts, params, option, function () {
              // 处理 change 事件相关逻辑
              handleConfirmFilter(params, !!option.data, option);
            }),
          });
        })
      ),
    ];
  };
}

/**
 * 检查触发源是否属于目标节点
 */
function getEventTargetNode(evnt: any, container: HTMLElement, className: string) {
  let targetElem;
  let target = evnt.target;
  while (target && target.nodeType && target !== document) {
    if (className && target.className && target.className.split && target.className.split(' ').indexOf(className) > -1) {
      targetElem = target;
    } else if (target === container) {
      return { flag: className ? !!targetElem : true, container, targetElem: targetElem };
    }
    target = target.parentNode;
  }
  return { flag: false };
}

/**
 * 事件兼容性处理
 */
function handleClearEvent(params: any, e: any) {
  const bodyElem: HTMLElement = document.body;
  const evnt = params.$event || e;
  if (
    // 下拉框
    getEventTargetNode(evnt, bodyElem, 'ant-select-dropdown').flag ||
    // 级联
    getEventTargetNode(evnt, bodyElem, 'ant-cascader-menus').flag ||
    // 日期
    getEventTargetNode(evnt, bodyElem, 'ant-calendar-picker-container').flag ||
    // 时间选择
    getEventTargetNode(evnt, bodyElem, 'ant-time-picker-panel').flag ||
    // 模态窗口
    getEventTargetNode(evnt, bodyElem, 'ant-modal-root').flag
  ) {
    return false;
  }
}
export const VXEAntdJhi = {
  install({ interceptor, renderer, formats }: typeof VXETable) {
    renderer.mixin(renderMap);
    interceptor.add('event.clearFilter', handleClearEvent);
    interceptor.add('event.clearActived', handleClearEvent);
    formats.add('momentFormatter', ({ cellValue }, pattern) => {
      return moment(cellValue).format(pattern || 'YYYY-MM-DD hh:mm:ss');
    });
    // 创建一个简单的超链接渲染
    VXETable.renderer.add('AAvatar', {
      renderDefault(h, renderOpts, params) {
        var row = params.row,
          column = params.column;
        var attrs = renderOpts.attrs || {};
        var cellValue = row[column.property];
        if (cellValue) {
          attrs.src = cellValue;
        } else {
          attrs.icon = 'user';
        }
        return [
          h('a-avatar', {
            attrs: attrs,
          }),
        ];
      },
    });
  },
};

if (typeof window !== 'undefined' && window.VXETable) {
  window.VXETable.use(VXEAntdJhi);
}

export default VXEAntdJhi;
