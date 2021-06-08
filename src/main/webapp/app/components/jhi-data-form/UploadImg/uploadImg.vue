<!--
 * @Description: 对上传图片组件进行封装
 * @Author: kcz
 * @Date: 2020-03-17 12:53:50
 * @LastEditors: kcz
 * @LastEditTime: 2020-03-29 22:03:12
 -->
<template>
  <div :style="{ width: record.options.width }" class="upload-img-box-9136076486841527">
    <a-upload
      :name="config.uploadImageName || record.options.fileName"
      :headers="config.uploadImageHeaders || record.options.headers"
      :data="config.uploadImageData || optionsData"
      :multiple="record.options.multiple"
      :listType="record.options.listType"
      :disabled="record.options.disabled || parentDisabled"
      :fileList="fileList"
      :action="config.uploadImage || record.options.action"
      :open-file-dialog-on-click="false"
      accept="image/gif, image/jpeg, image/png"
      @change="handleChange"
      @preview="handlePreview"
      :remove="remove"
      :beforeUpload="beforeUpload"
    >
      <a-button
        v-if="record.options.listType !== 'picture-card' && fileList.length < record.options.limit"
        :disabled="record.options.disabled || parentDisabled"
      >
        <a-icon type="upload" @click="uploadImage" />
        {{ record.options.placeholder }}
      </a-button>
      <div
        v-if="record.options.listType === 'picture-card' && fileList.length < record.options.limit"
        :disabled="record.options.disabled || parentDisabled"
      >
        <a-icon type="plus" @click="uploadImage" />
        <div class="ant-upload-text">{{ record.options.placeholder }}</div>
      </div>
    </a-upload>
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewImageUrl" />
    </a-modal>
  </div>
</template>
<script>
/*
 * author kcz
 * date 2019-12-31
 * description 上传图片组件
 */
export default {
  name: 'KUploadImg',
  // eslint-disable-next-line vue/require-prop-types
  props: ['record', 'value', 'config', 'parentDisabled'],
  data() {
    return {
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',
    };
  },
  watch: {
    value: {
      // value 需要深度监听及默认先执行handler函数
      handler(val) {
        if (val) {
          this.setFileList();
        }
      },
      immediate: true,
      deep: true,
    },
  },
  computed: {
    optionsData() {
      try {
        return JSON.parse(this.record.options.data);
      } catch {
        return {};
      }
    },
  },
  methods: {
    setFileList() {
      // 当传入value改变时，fileList也要改变
      // 如果传入的值为字符串，则转成json
      if (typeof this.value === 'string') {
        try {
          this.fileList = JSON.parse(this.value);
        } catch (err) {
          // 保留字符串，转换为file对象并转入this.fileList
          const imgFile = {
            name: 'image',
            status: 'done',
            uid: '0001',
            url: this.value,
          };
          this.fileList = [imgFile];
          this.record.options.limit = 1;
        }
        // 将转好的json覆盖组件默认值的字符串
        this.handleSelectChange();
      } else {
        this.fileList = this.value;
      }
    },
    handleSelectChange() {
      setTimeout(() => {
        const arr = this.fileList.map(item => {
          if (typeof item.response !== 'undefined') {
            const res = item.response;
            return {
              type: 'img',
              name: item.name,
              status: item.status,
              uid: res.id,
              url: res.url || '',
            };
          } else {
            return {
              type: 'img',
              name: item.name,
              status: item.status,
              uid: item.uid,
              url: item.url || '',
            };
          }
        });

        this.$emit('change', arr);
        this.$emit('input', arr);
      }, 10);
    },
    handlePreview(file) {
      // 预览图片
      this.previewImageUrl = file.url || file.thumbUrl;
      this.previewVisible = true;
    },
    handleCancel() {
      // 取消操作
      this.previewVisible = false;
    },
    remove() {
      this.handleSelectChange();
    },
    beforeUpload(e, files) {
      if (files.length + this.fileList.length > this.record.options.limit) {
        this.$message.warning(`最大上传数量为${this.record.options.limit}`);
        files.splice(this.record.options.limit - this.fileList.length);
      }
    },
    handleChange(info) {
      // 上传数据改变时
      this.fileList = info.fileList;
      if (info.file.status === 'done') {
        const res = info.file.response;
        this.handleSelectChange();
      } else if (info.file.status === 'error') {
        this.$message.error(`图片上传失败`);
      }
    },
    uploadImage() {
      this.$modalUpload(function (img) {
        console.log('testModalUpload', img);
      });
    },
  },
};
</script>
<style lang="less">
.upload-img-box-9136076486841527 {
  /* you can make up upload button and sample style by using stylesheets */

  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
}
</style>
