<!--
 * @Description: 对单个上传图片组件进行封装
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
      :multiple="false"
      :show-upload-list="false"
      list-type="picture-card"
      :disabled="record.options.disabled || parentDisabled"
      :action="config.uploadImage || record.options.action"
      accept="image/gif, image/jpeg, image/png"
      @change="handleChange"
      :remove="remove"
    >
      <img v-if="url" :src="url" class="avatar-img" alt="avatar" />
      <div v-else :disabled="record.options.disabled || parentDisabled">
        <div class="ant-upload-text">
          {{ record.options.placeholder || '上传' }}
        </div>
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
  name: 'KSingleUploadImg',
  // eslint-disable-next-line vue/require-prop-types
  props: ['record', 'value', 'config', 'parentDisabled', 'imageUrlField', 'dynamicData'],
  data() {
    return {
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',
      url: this.dynamicData.entity[this.imageUrlField],
    };
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
        this.url = arr[0].url;
        this.$emit('change', arr[0].uid);
        this.$emit('input', arr[0].uid);
      }, 10);
    },
    handlePreview() {
      // 预览图片
      this.previewImageUrl = this.url;
      this.previewVisible = true;
    },
    handleCancel() {
      // 取消操作
      this.previewVisible = false;
    },
    remove() {
      this.handleSelectChange();
    },
    handleChange(info) {
      // 上传数据改变时
      if (info.file.status === 'done') {
        this.url = info.file.response.url;
        this.$emit('change', info.file.response.id);
        this.$emit('input', info.file.response.id);
      } else if (info.file.status === 'error') {
        this.$message.error(`图片上传失败`);
      }
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
  .avatar-img {
    height: 96px;
    width: 96px;
  }
}
</style>
