import { RouteConfig } from 'vue-router';
import { PageView } from '@/layouts';
import { departmentRoute } from '@/entities/settings/department/department.route';
import { businessTypeRoute } from '@/entities/settings/business-type/business-type.route';
import { administrativeDivisionRoute } from '@/entities/settings/administrative-division/administrative-division.route';
import { positionRoute } from '@/entities/settings/position/position.route';
import { dataDictionaryRoute } from '@/entities/settings/data-dictionary/data-dictionary.route';
import { gpsInfoRoute } from '@/entities/settings/gps-info/gps-info.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

export const settingsRoute: RouteConfig = {
  path: 'settings',
  component: PageView,
  name: 'settings',
  meta: { authorities: ['ROLE_USER'], title: '配置管理' },
  children: [
    departmentRoute,
    businessTypeRoute,
    administrativeDivisionRoute,
    positionRoute,
    dataDictionaryRoute,
    gpsInfoRoute,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
