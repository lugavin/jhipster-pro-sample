import Switch from "ant-design-vue/es/switch";
import "ant-design-vue/lib/switch/style/css";

export default {
  name: "EditSwitch",
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
  mounted() {
    this.value = this.data;
  },
  methods: {
    check() {
      this.$nextTick(function() {
        this.$refs.EditSwitch.$el.style = "border-color:#409eff;";
      });
    },
    onChange(e) {
      const callbackRes = this.callBack("beforeChange");
      if (callbackRes === false) return;
      const checked = e;
      this.value = e;
      const valid = this.callBack("verify");
      if (valid === false) {
        this.$refs.EditSwitch.$el.style = "border-color:#f00";
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
      ref: "EditSwitch",
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
    return <Switch {...props} />;
  }
};
