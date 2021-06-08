import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const cacheManageRoute: RouteConfig = {
  path: 'cache-manage',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '个人信息' },
  children: [
    {
      path: '',
      name: 'system-cache-manage',
      component: () => import('./cache-manage.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
  ],
};
