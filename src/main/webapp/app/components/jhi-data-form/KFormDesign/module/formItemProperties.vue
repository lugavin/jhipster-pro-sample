<template>
  <div class="properties-centent kk-checkbox">
    <div class="head-title">控件属性设置</div>
    <div class="properties-body">
      <p class="hint-box" v-show="selectItem.key === ''">未选择控件</p>
      <a-form v-show="selectItem.key !== ''">
        <a-form-item v-if="typeof selectItem.label !== 'undefined'" label="标签">
          <a-input v-model="selectItem.label" placeholder="请输入" />
        </a-form-item>

        <a-form-item v-if="!hideModel && typeof selectItem.model !== 'undefined'" label="数据字段">
          <a-input v-model="selectItem.model" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="typeof options.labelColSpan !== 'undefined'" label="标签占用栅格">
          <a-input-number placeholder="栅格数" :min="0" :max="12" v-model="options.labelColSpan" />
        </a-form-item>
        <!-- input type start -->
        <a-form-item v-if="selectItem.type === 'input'" label="输入框type">
          <a-input v-model="options.type" placeholder="请输入" />
        </a-form-item>
        <!-- input type end -->
        <a-form-item v-if="typeof options.rangePlaceholder !== 'undefined' && options.range" label="占位内容">
          <a-input placeholder="请输入" v-model="options.rangePlaceholder[0]" />
          <a-input placeholder="请输入" v-model="options.rangePlaceholder[1]" />
        </a-form-item>

        <a-form-item v-else-if="typeof options.placeholder !== 'undefined'" label="占位内容">
          <a-input placeholder="请输入" v-model="options.placeholder" />
        </a-form-item>
        <a-form-item label="显示控件表达式">
          <a-input placeholder="空为正常显示" v-model="options.showExpression" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'textarea'" label="自适应内容高度">
          <a-input-number style="width: 100%" v-model="options.minRows" placeholder="最小高度" />
          <a-input-number style="width: 100%" v-model="options.maxRows" placeholder="最大高度" />
        </a-form-item>
        <a-form-item v-if="typeof options.width !== 'undefined'" label="宽度">
          <a-input placeholder="请输入" v-model="options.width" />
        </a-form-item>
        <a-form-item v-if="typeof options.height !== 'undefined'" label="高度">
          <a-input-number v-model="options.height" />
        </a-form-item>
        <a-form-item v-if="typeof options.step !== 'undefined'" label="步长">
          <a-input-number v-model="options.step" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="typeof options.min !== 'undefined'" label="最小值">
          <a-input-number v-model="options.min" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="typeof options.max !== 'undefined'" label="最大值">
          <a-input-number v-model="options.max" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="typeof options.precision !== 'undefined'" label="数值精度">
          <a-input-number :min="0" :max="50" v-model="options.precision" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="typeof options.dictCode !== 'undefined'" label="dictCode">
          <a-input v-model="options.dictCode"></a-input>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'modalSelect'" label="数据源选项">
          <a-input v-model="options.selectListName">
            <a-tooltip slot="suffix" title="数据组件标签">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-model="options.commonTableName">
            <a-tooltip slot="suffix" title="布局名称">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-model="options.parentId" :readonly="true">
            <a-tooltip slot="suffix" title="树形结构父Id">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
        </a-form-item>
        <!-- cocemirror属性 start -->
        <a-form-item v-if="selectItem.type === 'codemirror'" label="代码风格">
          <a-select v-model="options.mode" :options="codemirrorModes"></a-select>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'codemirror'" label="显示行号">
          <a-switch v-model="options.lineNumbers"></a-switch>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'codemirror'" label="自动换行">
          <a-switch v-model="options.lineWrapping"></a-switch>
        </a-form-item>
        <!-- cocemirror属性 end -->
        <a-form-item v-if="selectItem.type === 'treeSelect'" label="TreeSelect配置">
          <a-input v-model="options.replaceFields.children">
            <a-tooltip slot="suffix" title="子节点">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-model="options.replaceFields.title">
            <a-tooltip slot="suffix" title="标题属性名">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-model="options.replaceFields.value">
            <a-tooltip slot="suffix" title="值属性名">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-model="options.replaceFields.key">
            <a-tooltip slot="suffix" title="key属性">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
        </a-form-item>
        <!-- 选项配置及动态数据配置 start -->
        <a-form-item v-if="typeof options.options !== 'undefined'" label="选项配置">
          <a-radio-group buttonStyle="solid" v-model="options.dynamic">
            <a-radio-button :value="false">静态数据</a-radio-button>
            <a-radio-button :value="true">动态数据</a-radio-button>
          </a-radio-group>

          <a-input v-show="options.dynamic" v-model="options.dynamicKey" placeholder="动态数据变量名">
            <a-tooltip slot="suffix" title="动态数据变量名">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-show="options.dynamic" v-model="options.valueField" placeholder="value属性名">
            <a-tooltip slot="suffix" title="value属性名">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>
          <a-input v-show="options.dynamic" v-model="options.labelField" placeholder="lable属性名">
            <a-tooltip slot="suffix" title="lable属性名">
              <a-icon type="info-circle" style="color: rgba(0, 0, 0, 0.45)" />
            </a-tooltip>
          </a-input>

          <KChangeOption v-show="!options.dynamic" v-model="options.options" />
        </a-form-item>
        <!-- 选项配置及动态数据配置 end -->
        <a-form-item v-if="selectItem.type === 'grid'" label="栅格间距">
          <a-input-number v-model="selectItem.options.gutter" placeholder="请输入" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'grid'" label="列配置项">
          <KChangeOption v-model="selectItem.columns" type="colspan" />
        </a-form-item>

        <a-form-item v-if="selectItem.type === 'switch'" label="默认值">
          <a-switch v-model="options.defaultValue" />
        </a-form-item>
        <a-form-item v-if="['number', 'slider'].indexOf(selectItem.type) >= 0" label="默认值">
          <a-input-number
            :step="options.step"
            :min="options.min || -Infinity"
            :max="options.max || Infinity"
            v-model="options.defaultValue"
          />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'rate'" label="默认值">
          <a-rate v-model="options.defaultValue" :allowHalf="options.allowHalf" :count="options.max" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'select'" label="默认值">
          <a-select :options="options.options" v-model="options.defaultValue" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'radio'" label="默认值">
          <a-radio-group :options="options.options" v-model="options.defaultValue" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'checkbox'" label="默认值">
          <a-checkbox-group :options="options.options" v-model="options.defaultValue" />
        </a-form-item>
        <!-- 日期选择器默认值 start -->
        <a-form-item v-if="selectItem.type === 'date'" label="默认值">
          <a-input
            v-if="!options.range"
            v-model="options.defaultValue"
            :placeholder="typeof options.format === 'undefined' ? '' : options.format"
          />
          <a-input
            v-if="options.range"
            v-model="options.rangeDefaultValue[0]"
            :placeholder="typeof options.format === 'undefined' ? '' : options.format"
          />
          <a-input
            v-if="options.range"
            v-model="options.rangeDefaultValue[1]"
            :placeholder="typeof options.format === 'undefined' ? '' : options.format"
          />
        </a-form-item>
        <!-- 日期选择器默认值 start -->
        <a-form-item
          v-if="
            !['number', 'radio', 'checkbox', 'date', 'rate', 'select', 'switch', 'slider', 'html'].includes(selectItem.type) &&
            typeof options.defaultValue !== 'undefined'
          "
          label="默认值"
        >
          <a-input v-model="options.defaultValue" :placeholder="typeof options.format === 'undefined' ? '请输入' : options.format" />
        </a-form-item>
        <!-- 修改html -->
        <a-form-item v-if="selectItem.type === 'html'" label="默认值">
          <a-textarea v-model="options.defaultValue" :autoSize="{ minRows: 4, maxRows: 8 }" />
        </a-form-item>
        <a-form-item v-if="typeof options.format !== 'undefined'" label="时间格式">
          <a-input v-model="options.format" placeholder="时间格式如：YYYY-MM-DD HH:mm:ss" />
        </a-form-item>

        <a-form-item v-if="typeof options.orientation !== 'undefined'" label="标签位置">
          <a-radio-group buttonStyle="solid" v-model="options.orientation">
            <a-radio-button value="left">左</a-radio-button>
            <a-radio-button value="">居中</a-radio-button>
            <a-radio-button value="right">右</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'button'" label="类型">
          <a-radio-group buttonStyle="solid" v-model="options.type">
            <a-radio value="primary">Primary</a-radio>
            <a-radio value="default">Default</a-radio>
            <a-radio value="dashed">Dashed</a-radio>
            <a-radio value="danger">Danger</a-radio>
          </a-radio-group>
        </a-form-item>
        <!-- 下载方式 start -->
        <a-form-item v-if="typeof options.downloadWay !== 'undefined'" label="下载方式">
          <a-radio-group buttonStyle="solid" v-model="options.downloadWay">
            <a-radio-button value="a">a标签</a-radio-button>
            <a-radio-button value="ajax">ajax</a-radio-button>
            <a-radio-button value="dynamic">动态函数</a-radio-button>
          </a-radio-group>
          <a-input v-show="options.downloadWay === 'dynamic'" v-model="options.dynamicFun" placeholder="动态函数名"></a-input>
        </a-form-item>
        <!-- 下载方式 end -->
        <a-form-item v-if="selectItem.type === 'button'" label="按钮操作">
          <a-radio-group buttonStyle="solid" v-model="options.handle">
            <a-radio-button value="submit">提交</a-radio-button>
            <a-radio-button value="reset">重置</a-radio-button>
            <a-radio-button value="dynamic">动态函数</a-radio-button>
          </a-radio-group>
          <a-input v-show="options.handle === 'dynamic'" v-model="options.dynamicFun" placeholder="动态函数名"></a-input>
          <a-input v-show="options.handle === 'dynamic'" v-model="options.parameter" placeholder="参数"></a-input>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'alert'" label="辅助描述">
          <a-input v-model="options.description"></a-input>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'alert'" label="类型">
          <a-radio-group buttonStyle="solid" v-model="options.type">
            <a-radio value="success">success</a-radio>
            <a-radio value="info">info</a-radio>
            <a-radio value="warning">warning</a-radio>
            <a-radio value="error">error</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'alert'" label="操作属性">
          <kCheckbox v-model="options.showIcon" label="显示图标" />
          <kCheckbox v-model="options.banner" label="无边框" />
          <kCheckbox v-model="options.closable" label="可关闭" />
        </a-form-item>
        <!-- 上传图片 -->
        <a-form-item v-if="selectItem.type === 'uploadImg'" label="样式">
          <a-radio-group buttonStyle="solid" v-model="options.listType">
            <a-radio-button value="text">text</a-radio-button>
            <a-radio-button value="picture">picture</a-radio-button>
            <a-radio-button value="picture-card">card</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'singleUploadImg'" label="Url字段">
          <a-input v-model="options.imageUrlField"></a-input>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'singleUploadFile'" label="Url字段">
          <a-input v-model="options.fileUrlField"></a-input>
        </a-form-item>
        <!-- 上传数量 -->
        <a-form-item v-if="typeof options.limit !== 'undefined'" label="最大上传数量">
          <a-input-number :min="1" v-model="options.limit" />
        </a-form-item>

        <!-- scrollY -->
        <a-form-item v-if="typeof options.scrollY !== 'undefined'" label="scrollY">
          <a-input-number :min="0" v-model="options.scrollY" />
        </a-form-item>

        <!-- 上传地址 -->
        <a-form-item v-if="typeof options.action !== 'undefined'" label="上传地址">
          <a-input v-model="options.action" placeholder="请输入"></a-input>
        </a-form-item>
        <!-- 文件name -->
        <a-form-item v-if="typeof options.fileName !== 'undefined'" label="文件name">
          <a-input v-model="options.fileName" placeholder="请输入"></a-input>
        </a-form-item>
        <!-- 上传额外参数 -->
        <a-form-item v-if="typeof options.data !== 'undefined'" label="额外参数（JSON格式）">
          <a-input v-model="options.data" placeholder="严格JSON格式"></a-input>
        </a-form-item>
        <!-- 文字对齐方式 -->
        <a-form-item v-if="selectItem.type === 'text'" label="文字对齐方式">
          <a-radio-group buttonStyle="solid" v-model="options.textAlign">
            <a-radio-button value="left">左</a-radio-button>
            <a-radio-button value="center">居中</a-radio-button>
            <a-radio-button value="right">右</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'text'" label="操作属性">
          <kCheckbox v-model="options.showRequiredMark" label="显示必选标记" />
        </a-form-item>

        <a-form-item
          v-if="
            typeof options.hidden !== 'undefined' ||
            typeof options.disabled !== 'undefined' ||
            typeof options.readonly !== 'undefined' ||
            typeof options.clearable !== 'undefined' ||
            typeof options.multiple !== 'undefined' ||
            typeof options.range !== 'undefined' ||
            typeof options.showTime !== 'undefined' ||
            typeof options.allowHalf !== 'undefined' ||
            typeof options.showInput !== 'undefined'
          "
          label="操作属性"
        >
          <kCheckbox v-if="typeof options.hidden !== 'undefined'" v-model="options.hidden" label="隐藏" />
          <kCheckbox v-if="typeof options.disabled !== 'undefined'" v-model="options.disabled" label="禁用" />
          <kCheckbox v-if="typeof options.readonly !== 'undefined'" v-model="options.readonly" label="只读" />
          <kCheckbox v-if="typeof options.clearable !== 'undefined'" v-model="options.clearable" label="可清除" />
          <kCheckbox v-if="typeof options.multiple !== 'undefined'" v-model="options.multiple" label="多选" />
          <kCheckbox v-if="typeof options.range !== 'undefined'" v-model="options.range" label="范围选择" />
          <kCheckbox v-if="typeof options.showTime !== 'undefined'" v-model="options.showTime" label="时间选择器" />
          <kCheckbox v-if="typeof options.allowHalf !== 'undefined'" v-model="options.allowHalf" label="允许半选" />
          <kCheckbox v-if="typeof options.showInput !== 'undefined'" v-model="options.showInput" label="显示输入框" />
          <kCheckbox v-if="typeof options.showLabel !== 'undefined'" v-model="options.showLabel" label="显示Label" />
          <kCheckbox v-if="typeof options.chinesization !== 'undefined'" v-model="options.chinesization" label="汉化" />
          <kCheckbox v-if="typeof options.hideSequence !== 'undefined'" v-model="options.hideSequence" label="隐藏序号" />
          <kCheckbox v-if="typeof options.drag !== 'undefined'" v-model="options.drag" label="允许拖拽" />
          <kCheckbox v-if="typeof options.showSearch !== 'undefined'" v-model="options.showSearch" label="可搜索" />
          <kCheckbox v-if="typeof options.treeCheckable !== 'undefined'" v-model="options.treeCheckable" label="可勾选" />
        </a-form-item>

        <a-form-item v-if="typeof selectItem.rules !== 'undefined' && selectItem.rules.length > 0" label="校验">
          <kCheckbox v-model="selectItem.rules[0].required" label="必填" />
          <a-input v-model="selectItem.rules[0].message" placeholder="必填校验提示信息" />
          <KChangeOption v-model="selectItem.rules" type="rules" />
        </a-form-item>

        <!-- 表格选项 -->
        <a-form-item v-if="selectItem.type === 'table'" label="表格样式CSS">
          <a-input v-model="selectItem.options.customStyle" />
        </a-form-item>
        <a-form-item v-if="selectItem.type === 'table'" label="属性">
          <kCheckbox v-model="selectItem.options.bordered" label="显示边框" />
          <kCheckbox v-model="selectItem.options.bright" label="鼠标经过点亮" />
          <kCheckbox v-model="selectItem.options.small" label="紧凑型" />
        </a-form-item>

        <a-form-item v-if="selectItem.type === 'table'" label="提示">
          <p style="line-height: 26px">请点击右键增加行列，或者合并单元格</p>
        </a-form-item>
      </a-form>
    </div>
    <div class="close-box" @click="$emit('handleHide')">
      <a-icon type="double-right" />
    </div>
  </div>
</template>
<script>
/*
 * author kcz
 * date 2019-11-20
 * description 表单控件属性设置组件,因为配置数据是引用关系，所以可以直接修改
 */
import KChangeOption from '../../KChangeOption/index.vue';
import kCheckbox from '../../KCheckbox/index.vue';

export default {
  name: 'formItemProperties',
  data() {
    return {
      options: {},
      codemirrorModes: [
        {
          value: 'css',
          label: 'CSS',
        },
        {
          value: 'javascript',
          label: 'Javascript',
        },
        {
          value: 'html',
          label: 'XML/HTML',
        },
        {
          value: 'x-java',
          label: 'Java',
        },
        {
          value: 'x-objectivec',
          label: 'Objective-C',
        },
        {
          value: 'x-python',
          label: 'Python',
        },
        {
          value: 'x-rsrc',
          label: 'R',
        },
        {
          value: 'x-sh',
          label: 'Shell',
        },
        {
          value: 'x-sql',
          label: 'SQL',
        },
        {
          value: 'x-swift',
          label: 'Swift',
        },
        {
          value: 'x-vue',
          label: 'Vue',
        },
        {
          value: 'markdown',
          label: 'Markdown',
        },
      ],
    };
  },
  watch: {
    selectItem(val) {
      this.options = val.options || {};
    },
  },
  props: {
    selectItem: {
      type: Object,
      required: true,
    },
    hideModel: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    KChangeOption,
    kCheckbox,
  },
};
</script>
