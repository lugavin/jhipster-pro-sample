import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const userRoute: RouteConfig = {
  path: 'user',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '个人信息' },
  children: [
    {
      path: '',
      name: 'system-user-list',
      component: () => import('./user.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'system-user-new',
      component: () => import('./user-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':userId/edit',
      name: 'system-user-edit',
      component: () => import('./user-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':userId/detail',
      name: 'system-user-detail',
      component: () => import('./user-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
