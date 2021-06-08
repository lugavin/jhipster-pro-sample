<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="ossConfig">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.filesOssConfig.detail.title')">OssConfig</span> {{ ossConfig.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.id')">ID</span>
          </dt>
          <dd>
            <span>{{ ossConfig.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.provider')">Provider</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.OssProvider.' + ossConfig.provider)">{{ ossConfig.provider }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.ossCode')">Oss Code</span>
          </dt>
          <dd>
            <span>{{ ossConfig.ossCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.endpoint')">Endpoint</span>
          </dt>
          <dd>
            <span>{{ ossConfig.endpoint }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.accessKey')">Access Key</span>
          </dt>
          <dd>
            <span>{{ ossConfig.accessKey }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.secretKey')">Secret Key</span>
          </dt>
          <dd>
            <span>{{ ossConfig.secretKey }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.bucketName')">Bucket Name</span>
          </dt>
          <dd>
            <span>{{ ossConfig.bucketName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.appId')">App Id</span>
          </dt>
          <dd>
            <span>{{ ossConfig.appId }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.region')">Region</span>
          </dt>
          <dd>
            <span>{{ ossConfig.region }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.remark')">Remark</span>
          </dt>
          <dd>
            <span>{{ ossConfig.remark }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.filesOssConfig.enabled')">Enabled</span>
          </dt>
          <dd>
            <span>{{ ossConfig.enabled }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="ossConfig.id"
          :to="{ name: 'OssConfigEdit', params: { ossConfigId: ossConfig.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./oss-config-details.component.ts"></script>
