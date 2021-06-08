<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="position">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.settingsPosition.detail.title')">Position</span> {{ position.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsPosition.id')">ID</span>
          </dt>
          <dd>
            <span>{{ position.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsPosition.code')">Code</span>
          </dt>
          <dd>
            <span>{{ position.code }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsPosition.name')">Name</span>
          </dt>
          <dd>
            <span>{{ position.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsPosition.sortNo')">Sort No</span>
          </dt>
          <dd>
            <span>{{ position.sortNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsPosition.description')">Description</span>
          </dt>
          <dd>
            <span>{{ position.description }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="position.id"
          :to="{ name: 'PositionEdit', params: { positionId: position.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./position-details.component.ts"></script>
