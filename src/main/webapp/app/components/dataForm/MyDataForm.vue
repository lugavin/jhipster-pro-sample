<template>
  <a-form :form="form" @submit="handleSubmit">
    <a-form-item v-for="(item, index) in dataContent" :key="item.name" v-bind="formItemLayout" :label="item.label" :required="false">
      <a-input
        v-if="item.type === 'text'"
        :size="item.size ? item.size : 'default'"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : '',
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :placeholder="item.placeholder"
      />
      <a-switch
        v-else-if="item.type === 'switch'"
        :size="item.size ? item.size : 'default'"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : undefined,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
            valuePropName: 'checked',
          },
        ]"
      />
      <a-select
        v-else-if="item.type === 'select'"
        style="width: 100%"
        showSearch
        :filterOption="selectFilterOption"
        :size="item.size ? item.size : 'default'"
        allowClear
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : undefined,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :placeholder="item.placeholder"
      >
        <a-select-option v-for="option in item.options" :value="option[item.valueField]" :key="option[item.valueField]">{{
          option[item.labelField]
        }}</a-select-option>
      </a-select>
      <a-input-number
        v-else-if="item.type === 'number'"
        :size="item.size ? item.size : 'default'"
        :min="item.min ? item.min : 1"
        style="width: 100%"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : '',
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :placeholder="item.placeholder"
      />
      <a-radio-group
        v-else-if="item.type === 'radio' && Array.isArray(item.options)"
        :size="item.size ? item.size : 'default'"
        buttonStyle="solid"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : '',
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
      >
        <template v-for="(radioItem, index) in item.options">
          <a-radio-button :key="index" :value="radioItem.value">{{ radioItem.label }} </a-radio-button>
        </template>
      </a-radio-group>
      <a-date-picker
        v-else-if="item.type === 'datetime'"
        :size="item.size ? item.size : 'default'"
        :placeholder="item.placeholder"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : null,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
      />
      <a-range-picker
        v-else-if="item.type === 'datetimeRange'"
        :size="item.size ? item.size : 'default'"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : null,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :placeholder="item.placeholder"
      />
      <a-cascader
        v-else-if="item.type === 'cascader'"
        :size="item.size ? item.size : 'default'"
        :options="item.options"
        :showSearch="{ cascaderFilter }"
        v-decorator="[item.fieldName, { initialValue: item.defaultValue ? item.defaultValue : [] }]"
        :placeholder="item.placeholder"
      />
      <a-time-picker
        v-else-if="item.type === 'timepicker'"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : null,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :size="item.size ? item.size : 'default'"
      />

      <a-textarea
        v-else-if="item.type === 'textarea'"
        :placeholder="item.placeholder"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : null,
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
        :autosize="{ minRows: 6, maxRows: 24 }"
      />

      <a-select
        v-else-if="item.type === 'multiple'"
        mode="multiple"
        :size="item.size ? item.size : 'default'"
        optionFilterProp="children"
        :placeholder="item.placeholder"
        style="width: 100%"
        :options="item.options"
        v-decorator="[
          item.fieldName,
          {
            initialValue: item.defaultValue ? item.defaultValue : [],
            rules: Array.isArray(item.rules) && item.rules.length > 0 ? item.rules : [],
          },
        ]"
      />
    </a-form-item>
    <slot></slot>
    <a-form-item v-bind="formItemLayoutWithOutLabel">
      <a-button type="dashed" style="width: 60%" @click="add"> <a-icon type="plus" /> Add field </a-button>
    </a-form-item>
    <a-form-item v-bind="formItemLayoutWithOutLabel">
      <a-button type="primary" html-type="submit"> Submit </a-button>
    </a-form-item>
  </a-form>
</template>

<script>
let id = 0;
export default {
  props: {
    dataContent: Array,
  },
  data() {
    return {
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
      },
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 24, offset: 0 },
          sm: { span: 20, offset: 4 },
        },
      },
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'dynamic_form_item' });
    // this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
  },
  methods: {
    remove(k) {
      const { form } = this;
      // can use data-binding to get
      const keys = form.getFieldValue('keys');
      // We need at least one passenger
      if (keys.length === 1) {
        return;
      }

      // can use data-binding to set
      form.setFieldsValue({
        keys: keys.filter(key => key !== k),
      });
    },

    add() {
      // const { form } = this;
      // can use data-binding to get
      // const keys = form.getFieldValue('keys');
      this.dataContent = this.dataContent.concat(id++);
      // can use data-binding to set
      // important! notify form to detect changes
      /*form.setFieldsValue({
                    keys: nextKeys,
                });*/
    },

    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          this.$emit('submit', values);
        }
      });
    },
    selectFilterOption(input, option) {
      // 下拉框过滤函数
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    cascaderFilter(inputValue, path) {
      // 级联过滤函数
      return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1);
    },
  },
};
</script>
<style>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
