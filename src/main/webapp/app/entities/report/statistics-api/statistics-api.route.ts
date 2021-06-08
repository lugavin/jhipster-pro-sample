import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const statisticsApiRoute: RouteConfig = {
  path: 'statistics-api',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '统计Api' },
  children: [
    {
      path: '',
      name: 'report-statistics-api-list',
      component: () => import('./statistics-api.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'report-statistics-api-new',
      component: () => import('./statistics-api-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':statisticsApiId/edit',
      name: 'report-statistics-api-edit',
      component: () => import('./statistics-api-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':statisticsApiId/detail',
      name: 'report-statistics-api-detail',
      component: () => import('./statistics-api-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
