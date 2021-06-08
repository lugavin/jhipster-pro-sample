<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="gpsInfo">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.detail.title')">GpsInfo</span> {{ gpsInfo.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.id')">ID</span>
          </dt>
          <dd>
            <span>{{ gpsInfo.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.type')">Type</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.GpsType.' + gpsInfo.type)">{{ gpsInfo.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.latitude')">Latitude</span>
          </dt>
          <dd>
            <span>{{ gpsInfo.latitude }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.longitude')">Longitude</span>
          </dt>
          <dd>
            <span>{{ gpsInfo.longitude }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsGpsInfo.address')">Address</span>
          </dt>
          <dd>
            <span>{{ gpsInfo.address }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="gpsInfo.id"
          :to="{ name: 'GpsInfoEdit', params: { gpsInfoId: gpsInfo.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./gps-info-details.component.ts"></script>
