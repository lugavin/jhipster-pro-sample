import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const smsConfigRoute: RouteConfig = {
  path: 'sms-config',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '短信配置' },
  children: [
    {
      path: '',
      name: 'files-sms-config-list',
      component: () => import('./sms-config.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'files-sms-config-new',
      component: () => import('./sms-config-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':smsConfigId/edit',
      name: 'files-sms-config-edit',
      component: () => import('./sms-config-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':smsConfigId/detail',
      name: 'files-sms-config-detail',
      component: () => import('./sms-config-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
