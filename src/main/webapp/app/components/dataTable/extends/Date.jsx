import DatePicker from "ant-design-vue/es/date-picker";
import "ant-design-vue/lib/date-picker/style/css";

import moment from "moment";
import "moment/locale/zh-cn";
import { zhCn } from "./../util/locale.js";
moment.locale("zh-cn");

export default {
  name: "EditDate",
  props: {
    data: {
      type: [String, Number],
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
      status: false,
      locale: zhCn
    };
  },
  computed: {
    dateValue() {
      if (this.value) {
        return moment(this.value);
      }
      return moment();
    }
  },
  created() {},
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
        this.$refs.EditDate.focus();
        this.$refs.EditDate.$el.style = "border-color:#409eff;";
      });
    },
    onChange(date, dateString) {
      console.log(date, dateString);
      const callbackRes = this.callBack("beforeChange");
      if (callbackRes === false) return;
      this.value = dateString;
      this.updateData();
      this.$emit("onEnter");
    },
    updateData() {
      this.$emit("change", this.value);
      this.$emit("update", this.value);
      //修改后回调
      this.callBack("change");
      this.status = false;
      this.editable = false;
    },
    onOpenChange(status) {
      this.status = status;
      if (status == false) {
        this.onBlur(status);
      }
    },
    onBlur(e) {
      console.log(e);
      if (!this.status) {
        this.editable = false;
      }
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
        this.check();
      },
      keydown: e => {
        e.stopPropagation();
        if (e.keyCode == 9 || e.keyCode == 13 || e.keyCode == 39) {
          e.preventDefault();
          this.updateData();
          this.$emit("onEnter");
        } else if (e.keyCode == 37) {
          e.preventDefault();
          this.updateData();
          this.$emit("onBackEnter");
        } else if (e.keyCode == 38) {
          e.preventDefault();
          this.updateData();
          this.$emit("onUpEnter");
        } else if (e.keyCode == 40) {
          e.preventDefault();
          this.updateData();
          this.$emit("onDownEnter");
        }
      }
    };
    const props = {
      props: {
        ...this.column.edit.props,
        locale: this.locale
      },
      ref: "EditDate",
      on: {
        change: (date, dateString) => {
          this.onChange(date, dateString);
        },
        blur: e => {
          this.onBlur(e);
        },
        openChange: e => {
          this.onOpenChange(e);
        }
      }
    };
    if (this.value) {
      props.props.value = this.dateValue;
    }
    let input = "";
    if (this.editable) {
      input = (
        <div class="input-wrapper">
          <DatePicker {...props} />
        </div>
      );
    } else {
      input = <div class="text-wrapper">{this.value}</div>;
    }
    return (
      <div class="editable-cell" {...{ on: onCheck }}>
        {input}
      </div>
    );
  }
};
