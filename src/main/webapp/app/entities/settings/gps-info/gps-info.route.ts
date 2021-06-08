import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const gpsInfoRoute: RouteConfig = {
  path: 'gps-info',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: 'GPS信息' },
  children: [
    {
      path: '',
      name: 'settings-gps-info-list',
      component: () => import('./gps-info.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'settings-gps-info-new',
      component: () => import('./gps-info-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':gpsInfoId/edit',
      name: 'settings-gps-info-edit',
      component: () => import('./gps-info-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':gpsInfoId/detail',
      name: 'settings-gps-info-detail',
      component: () => import('./gps-info-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
