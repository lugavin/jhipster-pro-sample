<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="person">
        <h2 class="jh-entity-heading"><span v-text="$t('testnew11App.systemPerson.detail.title')">Person</span> {{ person.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('testnew11App.systemPerson.birthday')">Birthday</span>
          </dt>
          <dd>
            <span v-if="person.birthday">{{ $d(Date.parse(person.birthday), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('testnew11App.systemPerson.mobile')">Mobile</span>
          </dt>
          <dd>
            <span>{{ person.mobile }}</span>
          </dd>
          <dt>
            <span v-text="$t('testnew11App.systemPerson.user')">User</span>
          </dt>
          <dd>
            {{ person.userLogin }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="person.id" :to="{ name: 'PersonEdit', params: { personId: person.id } }" tag="button" class="btn btn-primary">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-details.component.ts"></script>
