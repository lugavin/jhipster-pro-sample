<template>
  <a-row>
    <a-select
      @click.native="showModal($event)"
      :open="false"
      :disabled="props.disabled"
      v-model="value"
      :mode="props.mode"
      style="width: 100%"
    >
      <a-icon slot="suffixIcon" type="profile" />
      <a-select-option v-for="option in options" :value="option[props.valueField]" :key="option[props.valueField]">
        {{ option[props.labelField] }}
      </a-select-option>
    </a-select>
    <a-modal
      title="请选择"
      :destroyOnClose="true"
      :footer="null"
      :bodyStyle="{ padding: '6px' }"
      :visible="modalVisible"
      :maskClosable="false"
      @cancel="cancelHideModal"
      @ok="okHideModal"
    >
      <component
        v-bind:is="selectListName"
        :commonTableName="''"
        @ok="okHideModal"
        @cancel="cancelHideModal"
        :select-ids="value"
        :select-model="props.mode"
        :row-id-name="optionProps.value"
        :parent-id="parentId"
      ></component>
      <!--          <slot name="listTable" @cancel="hideModal" @ok="hideModal"></slot>-->
    </a-modal>
  </a-row>
</template>

<script>
export default {
  name: 'select-list-modal',
  model: {
    prop: 'value',
    event: 'change',
  },
  data() {
    return {
      modalVisible: false,
      resultValue: null,
    };
  },
  props: {
    selectListName: {
      type: String,
      default: 'jhi-common-table-compact',
    },
    props: {
      type: Object,
      default: () => {
        return {};
      },
    },
    optionProps: {
      type: Object,
      default: () => {
        return { value: 'value', label: 'lable', disabled: 'disabled' };
      },
    },
    options: {
      type: Array,
      default: () => {
        return [];
      },
    },
    value: {
      type: String | Number,
      default: null,
    },
    parentId: {
      type: Number,
      default: null,
    },
  },
  methods: {
    showModal(e) {
      e.stopPropagation();
      e.preventDefault();
      this.modalVisible = true;
    },
    okHideModal(selectRecords) {
      if (this.props.mode !== 'multiple') {
        if (selectRecords) {
          const exitValue = this.options.some(option => option[this.optionProps.value] === selectRecords[this.optionProps.value]);
          if (!exitValue) {
            const newOption = {};
            newOption[this.optionProps.value] = selectRecords[this.optionProps.value];
            newOption[this.optionProps.label] = selectRecords[this.optionProps.label];
            this.options.push(newOption);
          }
          this.resultValue = selectRecords[this.optionProps.value];
        }
      } else {
        this.resultValue = [];
        if (selectRecords && selectRecords.length > 0) {
          selectRecords.forEach(record => {
            const exitValue = this.options.some(option => option[this.optionProps.value] === record[this.optionProps.value]);
            if (!exitValue) {
              const newOption = {};
              newOption[this.optionProps.value] = record[this.optionProps.value];
              newOption[this.optionProps.label] = record[this.optionProps.label];
              this.options.push(newOption);
            }
            this.resultValue.push(record[this.optionProps.value]);
          });
        }
      }
      this.$emit('change', this.resultValue);
      this.modalVisible = false;
    },
    cancelHideModal(e) {
      this.modalVisible = false;
    },
  },
};
</script>
