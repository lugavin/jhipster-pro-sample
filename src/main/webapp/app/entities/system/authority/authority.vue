<template>
  <a-card :bordered="false">
    <a-row :gutter="16">
      <a-col :span="filterTreeSpan" v-if="filterTreeSpan > 0">
        <a-tree
          style="border: #bbcedd 1px solid; height: 100%"
          v-model="checkedKeys"
          :expandedKeys="expandedKeys"
          :autoExpandParent="autoExpandParent"
          :selectedKeys="selectedKeys"
          :treeData="treeFilterData"
          @select="onSelect"
          @expand="onExpand"
        />
      </a-col>
      <a-col :span="24 - filterTreeSpan">
        <a-skeleton :loading="xGridColumns.length === 0">
          <vxe-grid
            border
            show-overflow
            keep-source
            ref="xGrid"
            :tree-config="xGridTreeConfig"
            :row-key="true"
            :row-id="'id'"
            :loading="loading"
            :data="xGridData"
            :columns="xGridColumns"
            :toolbar="xGridTableToolbars"
            :edit-config="{ trigger: 'click', mode: 'cell' }"
            :filter-config="xGridFilterConfig"
            :pager-config="xGridPagerConfig"
            :sort-config="xGridSortConfig"
            :show-overflow="'tooltip'"
            :show-header-overflow="'tooltip'"
            @checkbox-change="xGridCheckboxChangeEvent"
            @checkbox-all="xGridCheckboxChangeEvent"
            @edit-closed="editClosedEvent"
            @page-change="xGridPageChange"
            @sort-change="xGridSortChange"
            @filter-change="xGridFilterChange"
          >
            <template v-slot:top>
              <a-alert type="warning" :message="`已选择 ${xGridSelectRecords.length} 项`" banner></a-alert>
            </template>
            <template v-slot:toolbar_buttons>
              <a-row class="toolbar_buttons_xgrid" :gutter="16">
                <a-col :lg="2" :md="2" :sm="4" v-if="treeFilterData.length > 0">
                  <span class="table-page-search-submitButtons">
                    <a-button type="primary" :icon="filterTreeSpan > 0 ? 'pic-center' : 'pic-right'" @click="switchFilterTree"></a-button>
                  </span>
                </a-col>
                <a-col :lg="8" :md="12" :sm="9">
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
                <a-col :lg="14" :md="10" :sm="11" align="right">
                  <span class="table-page-search-submitButtons">
                    <a-button type="primary" icon="plus" @click="newEntity"></a-button>
                  </span>
                  <span class="table-page-search-submitButtons">
                    <a-button type="primary" icon="sync" @click="loadAll"></a-button>
                  </span>
                </a-col>
              </a-row>
            </template>
            <template slot-scope="{ row, column }" slot="recordAction">
              <a-tooltip placement="top" title="删除本行">
                <a-popconfirm title="确定删除本行数据吗？" @confirm="removeById(row.id)" okText="确定" cancelText="取消">
                  <a-button type="danger" shape="circle" size="small">
                    <a-icon type="close" theme="outlined"></a-icon>
                  </a-button>
                </a-popconfirm>
              </a-tooltip>
              <a-tooltip placement="top" title="编辑本行">
                <a-button type="primary" shape="circle" size="small" @click="editEntity(row)">
                  <a-icon type="setting"></a-icon>
                </a-button>
              </a-tooltip>
              <a-tooltip placement="top" title="增加下级">
                <a-button type="primary" shape="circle" size="small" @click="addChild(row)">
                  <a-icon type="enter"></a-icon>
                </a-button>
              </a-tooltip>
            </template>
          </vxe-grid>
        </a-skeleton>
      </a-col>
    </a-row>
    <a-modal
      title="新增或编辑"
      :visible="updateModalVisible"
      :destroyOnClose="true"
      :footer="null"
      @cancel="updateModalCancel"
      width="85%"
      :maskClosable="false"
    >
      <jhi-authority-update @cancel="updateModalCancel" :showInModal="true" :updateEntityId="authorityId" :parentId="parentId" />
    </a-modal>
  </a-card>
</template>

<script lang="ts" src="./authority.component.ts"></script>
<style>
.toolbar_buttons_xgrid {
  margin-left: 5px !important;
}
.table-page-search-submitButtons {
  display: inline-block !important;
}
</style>
