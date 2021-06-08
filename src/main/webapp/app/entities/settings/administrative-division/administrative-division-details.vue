<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="administrativeDivision">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.detail.title')">AdministrativeDivision</span>
          {{ administrativeDivision.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.id')">ID</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.name')">Name</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.areaCode')">Area Code</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.areaCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.cityCode')">City Code</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.cityCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.mergerName')">Merger Name</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.mergerName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.shortName')">Short Name</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.shortName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.zipCode')">Zip Code</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.zipCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.level')">Level</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.level }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.lng')">Lng</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.lng }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.lat')">Lat</span>
          </dt>
          <dd>
            <span>{{ administrativeDivision.lat }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsAdministrativeDivision.parent')">上级节点</span>
          </dt>
          <dd>
            <div v-if="administrativeDivision.parentId">
              <router-link
                :to="{ name: 'AdministrativeDivisionView', params: { administrativeDivisionId: administrativeDivision.parentId } }"
                >{{ administrativeDivision.parentName }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="administrativeDivision.id"
          :to="{ name: 'AdministrativeDivisionEdit', params: { administrativeDivisionId: administrativeDivision.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./administrative-division-details.component.ts"></script>
