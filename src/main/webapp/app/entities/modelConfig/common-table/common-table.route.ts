import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const commonTableRoute: RouteConfig = {
  path: 'common-table',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '模型' },
  children: [
    {
      path: '',
      name: 'modelConfig-common-table-list',
      component: () => import('./common-table.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'modelConfig-common-table-new',
      component: () => import('./common-table-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':commonTableId/edit',
      name: 'modelConfig-common-table-edit',
      component: () => import('./common-table-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':commonTableId/detail',
      name: 'modelConfig-common-table-detail',
      component: () => import('./common-table-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
    {
      path: ':commonTableId/designer',
      name: 'modelConfig-common-table-designer',
      component: () => import('./design-form.vue'),
      meta: { authorities: ['ROLE_USER'], title: '设计' },
    },
    {
      path: 'new/:copyFromId',
      name: 'modelConfig-common-table-copy',
      component: () => import('./common-table-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '复制' },
    },
  ],
};
