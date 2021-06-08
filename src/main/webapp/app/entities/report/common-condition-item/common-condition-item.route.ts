import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const commonConditionItemRoute: RouteConfig = {
  path: 'common-condition-item',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '通用条件条目' },
  children: [
    {
      path: '',
      name: 'report-common-condition-item-list',
      component: () => import('./common-condition-item.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'report-common-condition-item-new',
      component: () => import('./common-condition-item-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':commonConditionItemId/edit',
      name: 'report-common-condition-item-edit',
      component: () => import('./common-condition-item-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':commonConditionItemId/detail',
      name: 'report-common-condition-item-detail',
      component: () => import('./common-condition-item-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
