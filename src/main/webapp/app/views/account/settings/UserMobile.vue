<template>
  <a-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="手机号码">
          <a-input type="password" placeholder="请输入手机号码" v-decorator="['mobile', validatorRules.mobile]" />
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="短信验证码">
          <a-row :gutter="16">
            <a-col :span="16">
              <a-input placeholder="请输入短信验证码" v-decorator="['smsCode', validatorRules.smsCode]" />
            </a-col>
            <a-col :span="8">
              <a-button
                class="getCaptcha"
                tabindex="-1"
                :disabled="!smsSendBtn"
                @click.stop.prevent="sendSmsCode"
                v-text="(smsSendBtn && '获取验证码') || time + ' s'"
              ></a-button>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item v-if="captcha" :labelCol="labelCol" :wrapperCol="wrapperCol" label="图形验证码">
          <a-row :gutter="16">
            <a-col :span="16">
              <a-input placeholder="请输入图形验证码" v-decorator="['imageCode', validatorRules.imageCode]" />
            </a-col>
            <a-col :span="8">
              <img [src]="captcha" style="width: 150px; height: 50px" />
            </a-col>
          </a-row>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import Axios from 'axios-observable';
import qs from 'qs';

export default {
  name: 'UserMobile',
  data() {
    return {
      smsSendBtn: true,
      time: 60,
      captcha: '',
      title: '绑定手机',
      modalWidth: 800,
      visible: false,
      confirmLoading: false,
      validatorRules: {
        mobile: {
          rules: [
            { required: true, message: '请输入手机号码!' },
            { pattern: /^((1[3,5,8][0-9])|(14[5,7])|(17[0,5,6,7,8])|(19[7]))\d{8}$/, message: '请检查手机号是否正确' },
          ],
        },
        smsCode: {
          rules: [
            {
              required: true,
              message: '请输入短信验证码!',
            },
          ],
        },
        imageCode: {
          rules: [
            {
              required: true,
              message: '请输入图形验证码!',
            },
          ],
        },
      },
      confirmDirty: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      form: this.$form.createForm(this),
      username: '',
    };
  },
  methods: {
    show(username) {
      if (!username) {
        this.$message.warning('当前系统无登陆用户!');
      } else {
        this.username = username;
        this.form.resetFields();
        this.visible = true;
      }
    },
    handleCancel() {
      this.close();
    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.disableSubmit = false;
      this.selectedRole = [];
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields(['mobile', 'smsCode'], (err, values) => {
        if (!err) {
          that.confirmLoading = true;
          const { mobile, smsCode } = values;
          this.saveMobile(mobile, smsCode).subscribe(
            res => {
              that.$message.success('手机绑定成功。');
            },
            error => {
              that.$message.warning('手机绑定更新失败！');
              that.confirmLoading = false;
            },
            () => {
              that.confirmLoading = false;
            }
          );
        }
      });
    },
    saveMobile(mobile, code, imageCode = '') {
      return Axios.post(`api/mobile/current-user?imageCode=${imageCode}&mobile=${mobile}&code=${code}`);
    },
    validateToNextPassword(rule, value, callback) {
      const form = this.form;
      if (value && this.confirmDirty) {
        form.validateFields(['confirm'], { force: true });
      }
      callback();
    },
    compareToFirstPassword(rule, value, callback) {
      const form = this.form;
      if (value && value !== form.getFieldValue('password')) {
        callback('两次输入的密码不一样！');
      } else {
        callback();
      }
    },
    handleConfirmBlur(e) {
      const value = e.target.value;
      this.confirmDirty = this.confirmDirty || !!value;
    },
    sendSmsCode() {
      this.form.validateFields(['mobile'], (err, values) => {
        if (!err) {
          const { mobile } = values;
          console.log(mobile);
          if (this.captcha) {
            // 先验证图形验证码
            //
          } else {
            this.smsSendBtn = false;
            const interval = window.setInterval(() => {
              if (this.time-- <= 0) {
                this.time = 60;
                this.smsSendBtn = true;
                window.clearInterval(interval);
              }
            }, 1000);
            const hide = this.$message.loading('验证码发送中..', 0);
            const imageCode = '';
            this.getCurrentSmsCode(imageCode, mobile).subscribe(
              res => {
                setTimeout(hide, 2500);
                this.$notification['success']({
                  message: '提示',
                  description: '验证码获取成功，请查看手机短信。',
                  duration: 8,
                });
              },
              error => {
                setTimeout(hide, 1);
                clearInterval(interval);
                this.time = 60;
                this.smsSendBtn = true;
              }
            );
          }
        }
      });
    },
    getCurrentSmsCode(imageCode, mobile) {
      return Axios.get(`api/mobile/smscode/current-user?imageCode=${imageCode}&mobile=${mobile}`);
    },
  },
};
</script>
