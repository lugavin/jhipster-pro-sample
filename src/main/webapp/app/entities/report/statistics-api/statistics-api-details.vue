<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="statisticsApi">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.detail.title')">StatisticsApi</span> {{ statisticsApi.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.id')">ID</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.title')">Title</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.apiKey')">Api Key</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.apiKey }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.createAt')">Create At</span>
          </dt>
          <dd>
            <span v-if="statisticsApi.createAt">{{ $d(Date.parse(statisticsApi.createAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.updateAt')">Update At</span>
          </dt>
          <dd>
            <span v-if="statisticsApi.updateAt">{{ $d(Date.parse(statisticsApi.updateAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.sourceType')">Source Type</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.StatSourceType.' + statisticsApi.sourceType)">{{ statisticsApi.sourceType }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.apiBody')">Api Body</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.apiBody }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.result')">Result</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.result }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.updateInterval')">Update Interval</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.updateInterval }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.lastSQLRunTime')">Last SQL Run Time</span>
          </dt>
          <dd>
            <span v-if="statisticsApi.lastSQLRunTime">{{ $d(Date.parse(statisticsApi.lastSQLRunTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.enable')">Enable</span>
          </dt>
          <dd>
            <span>{{ statisticsApi.enable }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.commonTable')">所属表</span>
          </dt>
          <dd>
            <div v-if="statisticsApi.commonTableId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: statisticsApi.commonTableId } }">{{
                statisticsApi.commonTableName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.creator')">创建人</span>
          </dt>
          <dd>
            {{ statisticsApi.creatorFirstName }}
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportStatisticsApi.modifier')">修改人</span>
          </dt>
          <dd>
            {{ statisticsApi.modifierFirstName }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="statisticsApi.id"
          :to="{ name: 'StatisticsApiEdit', params: { statisticsApiId: statisticsApi.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./statistics-api-details.component.ts"></script>
