import Select from "ant-design-vue/es/select";
import "ant-design-vue/lib/select/style/css";

const { Option } = Select;

export default {
  name: "EditSelect",
  props: {
    data: {
      type: [String, Number, Array],
      default: ""
    },
    column: {
      type: Object,
      default() {
        return {};
      }
    },
    record: {
      type: Object,
      default() {
        return {};
      }
    },
    index: {
      type: Number,
      default: 0
    },
    eventConfig: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  model: {
    prop: "data",
    event: "updata"
  },
  data() {
    return {
      value: this.data,
      editable: false,
      options: []
    };
  },
  computed: {
    valueField() {
      return this.column.edit && this.column.edit.valueField
        ? this.column.edit.valueField
        : "Id";
    },
    textField() {
      return this.column.edit && this.column.edit.textField
        ? this.column.edit.textField
        : "Text";
    },
    text() {
      const data =
        this.column.edit && this.column.edit.data ? this.column.edit.data : [];
      let arr = [];
      if (Array.isArray(this.value)) {
        this.value.forEach(element => {
          const sel = data.find(item => item[this.valueField] == element);
          if (sel != undefined) {
            return arr.push(sel[this.textField]);
          }
        });
        return arr.join(",");
      } else {
        const sel = data.find(item => item[this.valueField] == this.value);
        return sel != undefined ? sel[this.textField] : "";
      }
    }
  },
  created() {
    if (this.column.edit && this.column.edit.data) {
      this.options = this.column.edit.data;
    }
  },
  methods: {
    check() {
      if (this.editable) {
        return;
      }
      //开启编辑前回调
      const callbackRes = this.callBack("open");
      if (callbackRes === false) return;
      this.editable = true;
      this.$nextTick(function() {
        this.$refs.EditSelect.focus();
        this.$refs.EditSelect.$el.style = "border-color:#409eff;";
      });
    },
    onChange(value) {
      const callbackRes = this.callBack("beforeChange");
      if (callbackRes === false) return;
      const that = this;
      let sel = "";
      if (typeof value === "object") {
        const selects = this.options.filter(
          item => value.indexOf(item[that.valueField]) > -1
        );
        sel = selects;
      } else {
        sel = this.options.find(item => item[that.valueField] === value);
      }
      this.value = value == undefined ? " " : value;
      this.$emit("updata", this.value);
      this.$emit("change", this.value, sel);
      // this.callBack("change");
    },
    onBlur() {
      this.updateData();
    },
    updateData() {
      const valid = this.callBack("verify");
      if (valid === false) {
        this.$refs.EditInput.$el.style = "border-color:#f00";
        return;
      }
      this.editable = false;
      this.$emit("change", this.value);
      this.$emit("update", this.value);

      //修改后回调
      this.callBack("change");
    },
    onPressEnter() {
      const callbackRes = this.callBack("pressEnter");
      if (callbackRes === false) return;
      this.onBlur();
      this.$emit("onEnter");
    },
    callBack(type) {
      const edit = this.column.edit;
      if (edit && edit.on && edit.on[type]) {
        const res = edit.on[type]({
          index: this.index,
          value: this.value,
          column: this.column,
          record: this.record
        });
        return res;
      }
    }
  },
  render() {
    const onCheck = {
      click: () => {
        console.log("click");
        this.check();
      },
      keydown: e => {
        e.stopPropagation();
        const config = this.eventConfig;
        if (
          (e.keyCode == 9 && config.tabToActive) ||
          (e.keyCode == 13 && config.enterToActive) ||
          (e.keyCode == 39 && config.rightArrowToActive)
        ) {
          e.preventDefault();
          this.onPressEnter();
        } else if (e.keyCode == 37 && config.leftArrowToActive) {
          //点击左键回退
          e.preventDefault();
          this.updateData();
          this.$emit("onBackEnter");
        } else if (e.keyCode == 38 && config.upArrowToActive) {
          //点击上键
          e.preventDefault();
          this.updateData();
          this.$emit("onUpEnter");
        } else if (e.keyCode == 40 && config.upArrowToActive) {
          //点击下键
          e.preventDefault();
          this.updateData();
          this.$emit("onDownEnter");
        }
      }
    };
    const props = {
      props: {
        allowClear: true,
        ...this.column.edit.props
      },
      ref: "EditSelect",
      on: {
        ...this.column.on,
        change: e => {
          this.onChange(e);
        },
        blur: e => {
          this.onBlur(e);
        },
        select: e => {
          this.onPressEnter(e);
        }
      }
    };
    if (typeof this.value === "object") {
      props.props.value = this.value;
    } else if (
      this.column.props &&
      this.column.props.mode === "multiple" &&
      !this.value
    ) {
      props.props.value = [];
    } else if (typeof this.value === "number") {
      props.props.value = this.value + "";
    } else if (this.value) {
      props.props.value = this.value;
    }
    //关键词搜索时，接管filterOption,增加obj数据返回

    if (props.props.filterOption) {
      props.props.filterOption = (input, option) => {
        const value = option.componentOptions.propsData.value;
        const objIndex = this.options.findIndex(
          item => item[this.valueField] + "" === value
        );
        const obj = this.options[objIndex];
        return this.column.edit.props.filterOption(input, option, obj);
      };
    }
    let input = "";

    if (this.editable) {
      const items = this.options.map(item => {
        return (
          <Option value={item[this.valueField] + ""}>
            {item[this.textField]}
          </Option>
        );
      });
      input = (
        <div class="input-wrapper">
          <Select style={"width:100%"} {...props}>
            {items}
          </Select>
        </div>
      );
    } else {
      input = <div class="text-wrapper">{this.text}</div>;
    }
    return (
      <div class="editable-cell" {...{ on: onCheck }}>
        {input}
      </div>
    );
  }
};
