import Checkbox from "ant-design-vue/es/checkbox";
import "ant-design-vue/lib/checkbox/style/css";

export default {
  name: "EditCheckbox",
  props: {
    data: {
      type: [Number, String, Boolean],
      default: false
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
    }
  },
  model: {
    prop: "data",
    event: "updata"
  },
  data() {
    return {
      value: false,
      editable: false
    };
  },
  created() {
    this.value = this.data;
  },
  methods: {
    check() {
      this.$nextTick(function() {
        this.$refs.EditCheckbox.$el.style = "border-color:#409eff;";
      });
    },
    onChange(e) {
      const callbackRes = this.callBack("beforeChange");
      if (callbackRes === false) return;
      const checked = e.target.checked;
      this.value = checked;
      const valid = this.callBack("verify");
      if (valid === false) {
        this.$refs.EditCheckbox.$el.style = "border-color:#f00";
        return;
      }
      this.$emit("updata", checked);
      this.$emit("change", checked);
      this.callBack("change");
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
    const that = this;
    const props = {
      ref: "EditCheckbox",
      props: {
        ...this.column.edit.props,
        defaultChecked: this.data
      },
      on: {
        change: e => {
          that.onChange(e);
        },
        focus: e => {
          this.$emit("focus", e);
        },
        blur: e => {
          this.$emit("blur", e);
        }
      }
    };
    props.props.value = this.value;
    return <Checkbox {...props} />;
  }
};
