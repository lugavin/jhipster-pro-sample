<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="uReportFile">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.reportUReportFile.detail.title')">UReportFile</span> {{ uReportFile.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.id')">ID</span>
          </dt>
          <dd>
            <span>{{ uReportFile.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.name')">Name</span>
          </dt>
          <dd>
            <span>{{ uReportFile.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.content')">Content</span>
          </dt>
          <dd>
            <span>{{ uReportFile.content }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.createAt')">Create At</span>
          </dt>
          <dd>
            <span v-if="uReportFile.createAt">{{ $d(Date.parse(uReportFile.createAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.updateAt')">Update At</span>
          </dt>
          <dd>
            <span v-if="uReportFile.updateAt">{{ $d(Date.parse(uReportFile.updateAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportUReportFile.commonTable')">关联表</span>
          </dt>
          <dd>
            <div v-if="uReportFile.commonTableId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: uReportFile.commonTableId } }">{{
                uReportFile.commonTableName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="uReportFile.id"
          :to="{ name: 'UReportFileEdit', params: { uReportFileId: uReportFile.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./u-report-file-details.component.ts"></script>
