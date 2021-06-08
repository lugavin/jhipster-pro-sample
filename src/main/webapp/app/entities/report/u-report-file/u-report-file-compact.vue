<template>
  <a-card :bordered="false" :bodyStyle="{ padding: 0 }">
    <a-row :gutter="16">
      <a-col :span="filterTreeSpan" v-if="filterTreeSpan > 0">
        <a-tree
          style="border: #bbcedd 1px solid; height: 100%"
          @expand="onExpand"
          :expandedKeys="expandedKeys"
          :autoExpandParent="autoExpandParent"
          v-model="checkedKeys"
          @select="onSelect"
          :selectedKeys="selectedKeys"
          :treeData="treeFilterData"
        />
      </a-col>
      <a-col :span="24 - filterTreeSpan">
        <vxe-grid
          border
          show-overflow
          tree-config
          keep-source
          ref="xGridCompact"
          :loading="loading"
          :data="xGridData"
          :columns="xGridColumns"
          :toolbar="xGridTableToolbars"
          :edit-config="{ trigger: 'click', mode: 'cell' }"
          :row-key="true"
          :row-id="rowIdName || 'id'"
          :pagerConfig="xGridPagerConfig"
          :checkbox-config="xGridCheckboxConfig"
          :radio-config="xGridRadioConfig"
          @radio-change="xGridRadioChangeEvent"
          @checkbox-change="xGridCheckboxChangeEvent"
          @checkbox-all="xGridCheckboxChangeEvent"
          @page-change="xGridPageChange"
        >
          <template v-slot:top>
            <a-alert
              type="warning"
              :message="`已选择 ${xGridSelectType === 'checkbox' ? xGridSelectRecords.length : !xGridRadioSelectRecord ? 0 : 1} 项`"
              banner
            ></a-alert>
          </template>
          <template v-slot:toolbar_buttons>
            <a-row class="toolbar_buttons_xgrid" :gutter="16">
              <a-col :md="12" :sm="24">
                <a-input-search
                  placeholder="请输入关键字"
                  v-model="searchValue"
                  allow-clear
                  @search="loadAll"
                  enterButton
                  ref="searchInput"
                >
                </a-input-search>
              </a-col>
              <!--<a-col :md="12" :sm="24">
                            <span class="table-page-search-submitButtons">
                                <a-button type="primary" icon="plus" @click="newEntity">新建</a-button>
                            </span>
                            </a-col> -->
            </a-row>
          </template>
        </vxe-grid>
      </a-col>
    </a-row>
    <a-row>
      <a-col :lg="8" :md="12" :sm="0"></a-col>
      <a-col :lg="16" :md="12" :sm="24">
        <span class="table-page-search-submitButtons" style="display: inline-block">
          <a-button type="primary" icon="close" @click="handleCancel">取消</a-button>
        </span>
        <span class="table-page-search-submitButtons" style="display: inline-block">
          <a-button
            type="primary"
            icon="check"
            :disabled="xGridSelectType === 'checkbox' ? xGridSelectRecords.length === 0 : !xGridRadioSelectRecord"
            @click="handleOK"
            >确定</a-button
          >
        </span>
      </a-col>
    </a-row>
  </a-card>
</template>

<script lang="ts" src="./u-report-file-compact.component.ts"></script>
<style>
.toolbar_buttons_xgrid {
  margin-left: 5px !important;
}
.table-page-search-submitButtons {
  display: inline-block !important;
}
</style>
