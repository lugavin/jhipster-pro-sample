import { RouteConfig } from 'vue-router';
import { PageView } from '@/layouts';
import { userRoute } from '@/entities/system/user/user.route';
import { authorityRoute } from '@/entities/system/authority/authority.route';
import { cacheManageRoute } from '@/entities/system/cache-manage/cache-manage.route';
import { apiPermissionRoute } from '@/entities/system/api-permission/api-permission.route';
import { viewPermissionRoute } from '@/entities/system/view-permission/view-permission.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

export const systemRoute: RouteConfig = {
  path: 'system',
  name: 'system',
  component: PageView,
  meta: { authorities: ['ROLE_USER'], title: '系统设置' },
  children: [
    cacheManageRoute,
    userRoute,
    authorityRoute,
    apiPermissionRoute,
    viewPermissionRoute,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
