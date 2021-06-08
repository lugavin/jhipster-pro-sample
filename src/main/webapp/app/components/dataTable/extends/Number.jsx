import InputNumber from "ant-design-vue/es/input-number";
import "ant-design-vue/lib/input-number/style/css";

export default {
  name: "FormNumber",
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
      editable: false
    };
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
        this.$refs.EditNumber.focus();
        this.$refs.EditNumber.$el.style = "border-color:#409eff;";
      });
    },
    onChange(e) {
      this.value = e ? e : 0;
    },
    onBlur(e) {
      console.log(e);
      //修改前回调
      const callbackRes = this.callBack("beforeChange");
      if (callbackRes === false) return;
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
      this.updateData();
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
        }
        // else if (e.keyCode == 38 && config.upArrowToActive) {
        //   //点击上键
        //   e.preventDefault();
        //   this.updateData();
        //   this.$emit("onUpEnter");
        // } else if (e.keyCode == 40 && config.upArrowToActive) {
        //   //点击下键
        //   e.preventDefault();
        //   this.updateData();
        //   this.$emit("onDownEnter");
        // }
      }
    };
    const props = {
      props: {
        min: 0,
        max: 10000,
        ...this.column.edit.props
      },
      ref: "EditNumber",
      on: {
        change: e => {
          this.onChange(e);
        },
        blur: e => {
          this.onBlur(e);
        }
        // keydown: e => {
        //   this.onPressEnter(e);
        // }
      }
    };
    if (this.value) {
      props.props.value = this.value;
    }
    let input = "";
    if (this.editable) {
      input = (
        <div class="input-wrapper">
          <InputNumber {...props} />
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
