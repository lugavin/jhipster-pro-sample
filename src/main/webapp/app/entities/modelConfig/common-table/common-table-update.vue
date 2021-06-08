<template>
  <a-card :body-style="{ padding: '24px 32px' }" :bordered="false">
    <k-form-build :value="formJsonData" ref="updateForm" :dynamicData="relationshipsData" />
    <a-tabs defaultActiveKey="1">
      <a-tab-pane :tab="$t('jhipsterApplicationApp.modelConfigCommonTable.commonTableFields')" key="1">
        <jhi-common-table-field
          :showInOther="true"
          :commonTableId="commonTable.id"
          :otherPresetOrder="{ order: 'asc' }"
        ></jhi-common-table-field>
      </a-tab-pane>
      <a-tab-pane :tab="$t('jhipsterApplicationApp.modelConfigCommonTable.relationships')" key="2">
        <jhi-common-table-relationship
          :showInOther="true"
          :commonTableId="commonTable.id"
          :otherPresetOrder="{ order: 'asc' }"
        ></jhi-common-table-relationship>
      </a-tab-pane>
    </a-tabs>
    <a-row type="flex" justify="center">
      <a-col span="3">
        <a-button @click="previousState()">{{ $t('entity.action.cancel') }}</a-button>
      </a-col>
      <a-col span="3">
        <a-button type="primary" @click="save">{{ $t('entity.action.save') }}</a-button>
      </a-col>
    </a-row>
  </a-card>
</template>
<script lang="ts" src="./common-table-update.component.ts"></script>
<style>
.ant-advanced-update-form .ant-form-item {
  display: flex;
}
.ant-advanced-update-form .ant-form-item-control-wrapper {
  flex: 1;
}
</style>
