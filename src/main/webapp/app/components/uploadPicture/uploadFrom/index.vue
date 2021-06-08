<template>
  <div>
    <a-modal title="上传图片" width="896px" :visible.sync="visible" :modal="false" :footer="null" append-to-body @cancel="handleClose">
      <upload-index v-if="visible" :isMore="isMore" :modelName="modelName" @getImage="getImage" />
    </a-modal>
  </div>
</template>

<script>
// import UploadIndex from '@/components/uploadPicture/index.vue'
export default {
  name: 'UploadFroms',
  // components: { UploadIndex },
  data() {
    return {
      visible: false,
      callback: function () {},
      isMore: '',
      modelName: '',
      ISmodal: false,
    };
  },
  watch: {
    // show() {
    //   this.visible = this.show
    // }
  },
  methods: {
    handleClose() {
      this.visible = false;
    },
    getImage(img) {
      this.callback(img);
      this.visible = false;
    },
  },
};
</script>

<style scoped></style>
