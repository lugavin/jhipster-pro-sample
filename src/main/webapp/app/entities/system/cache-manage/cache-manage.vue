<template>
  <a-card :bordered="false">
    <a-table :columns="columns" :data-source="data">
      <a slot="name" slot-scope="text">{{ text }}</a>
      <span slot="action" slot-scope="text, record">
        <a-button type="primary" icon="sync" @click="clear(text)"> 清除 </a-button>
      </span>
    </a-table>
  </a-card>
</template>

<script>
import Axios from 'axios-observable';
const columns = [
  {
    title: '缓存名称',
    dataIndex: 'name',
    key: 'name',
    slots: { title: 'customTitle' },
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
];

const data = [];

export default {
  data() {
    return {
      data,
      columns,
    };
  },
  created() {
    this.getAll();
  },
  methods: {
    getAll() {
      Axios.get('/api/cache-manage').subscribe(res => {
        res.data.forEach(cacheName => this.data.push({ name: cacheName }));
      });
    },
    clear(cacheName) {
      Axios.delete('/api/cache-manage/' + cacheName).subscribe(res => {
        this.$message.success('清除缓存成功。');
      });
    },
  },
};
</script>
