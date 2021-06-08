import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const commonConditionRoute: RouteConfig = {
  path: 'common-condition',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '通用条件' },
  children: [
    {
      path: '',
      name: 'report-common-condition-list',
      component: () => import('./common-condition.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'report-common-condition-new',
      component: () => import('./common-condition-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':commonConditionId/edit',
      name: 'report-common-condition-edit',
      component: () => import('./common-condition-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':commonConditionId/detail',
      name: 'report-common-condition-detail',
      component: () => import('./common-condition-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
