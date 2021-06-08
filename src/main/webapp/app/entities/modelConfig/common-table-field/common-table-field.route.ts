import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const commonTableFieldRoute: RouteConfig = {
  path: 'common-table-field',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '模型字段' },
  children: [
    {
      path: '',
      name: 'modelConfig-common-table-field-list',
      component: () => import('./common-table-field.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'modelConfig-common-table-field-new',
      component: () => import('./common-table-field-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':commonTableFieldId/edit',
      name: 'modelConfig-common-table-field-edit',
      component: () => import('./common-table-field-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':commonTableFieldId/detail',
      name: 'modelConfig-common-table-field-detail',
      component: () => import('./common-table-field-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
