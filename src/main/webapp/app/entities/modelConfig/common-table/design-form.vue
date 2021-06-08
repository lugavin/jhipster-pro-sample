<template>
  <a-card :body-style="{ padding: '24px 32px' }" :bordered="false">
    <k-form-design
      :showToolbarsText="true"
      :showHead="false"
      title="我是修改后的标题"
      @save="saveToServer"
      @close="closeDesign"
      ref="designer"
      toolbarsTop
    >
      <template slot="left-action">
        <a-tooltip title="从原始数据结构重新生成">
          <a @click="rebuildFromServer">
            <a-icon type="cloud-download" />
            <span>原始生成</span>
          </a>
        </a-tooltip>
      </template>
    </k-form-design>
  </a-card>
</template>
<script lang="ts" src="./design-form.component.ts"></script>
<style>
.ant-advanced-update-form .ant-form-item {
  display: flex;
}
.ant-advanced-update-form .ant-form-item-control-wrapper {
  flex: 1;
}
</style>
