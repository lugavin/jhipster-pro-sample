import BasicLayout from '@/layouts/BasicLayout.vue';
import { RouteConfig } from 'vue-router';
import { modelConfigRoute } from '@/entities/modelConfig/modelConfig.route';
import { filesRoute } from '@/entities/files/files.route';
import { systemRoute } from '@/entities/system/system.route';
import { reportRoute } from '@/entities/report/report.route';
import { settingsRoute } from '@/entities/settings/settings.route';
// jhipster-needle-add-client-root-folder-router-to-business-router-import - JHipster will import entities to the client root folder router here

export const entitiesRoute: RouteConfig = {
  path: '/entities',
  name: 'entities',
  component: BasicLayout,
  meta: { authorities: ['ROLE_ADMIN'], title: '业务' },
  children: [
    modelConfigRoute,
    filesRoute,
    systemRoute,
    reportRoute,
    settingsRoute,
    // jhipster-needle-add-client-root-folder-router-to-business-router - JHipster will import entities to the client root folder router here
  ],
};
