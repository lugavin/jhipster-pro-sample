import Axios from 'axios-observable';
import { Store } from 'vuex';
import store from '@/store';
import VueRouter from 'vue-router';
import TranslationService from '@/locale/translation.service';

import { ViewPermission } from '@/shared/model/system/view-permission.model';
import { TargetType } from '@/shared/model/enumerations/target-type.model';
import ViewPermissionService from '@/entities/system/view-permission/view-permission.service';
import { welcome } from '@/utils/util';

export default class AccountService {
  private viewPermissions: ViewPermission[] = [];
  constructor(
    private store: Store<any>,
    private translationService: TranslationService,
    private router: VueRouter,
    private viewPermissionService: ViewPermissionService
  ) {
    this.init();
  }

  public init(): void {
    const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
    if (!this.store.getters.account && !this.store.getters.logon && token) {
      this.retrieveAccount();
    }
    this.retrieveProfiles();
  }

  public retrieveProfiles(): void {
    Axios.get('management/info').subscribe(res => {
      /* if (res.data && res.data.activeProfiles) {
        this.store.commit('setRibbonOnProfiles', res.data['display-ribbon-on-profiles']);
        this.store.commit('setActiveProfiles', res.data['activeProfiles']);
      } */
    });
  }

  public retrieveAccount(): void {
    this.store.commit('authenticate');
    Axios.get('api/account').subscribe(
      response => {
        const account = response.data;
        if (account) {
          this.store.commit('authenticated', account);
          if (this.store.getters.currentLanguage !== account.langKey) {
            this.store.commit('currentLanguage', account.langKey);
          }
          // 设置用户信息
          this.store.commit('SET_MOBILE', account.mobile);
          this.store.commit('SET_NAME', { name: account.firstName, welcome: welcome() });
          this.store.commit('SET_AVATAR', account.imageUrl);

          // 获得所有菜单数据从服务器端
          this.viewPermissionService.treeByLogin().subscribe(
            res => {
              // 去除最外部的分组信息
              this.viewPermissions = res.data.reduce((prev, cur) => prev.concat(cur.children), []);
              // 处理菜单
              store
                .dispatch('GenerateRoutes', {
                  authorities: account.authorities,
                  viewPermissions: [
                    {
                      path: '/',
                      children: this.transformViewPermissionsToMenus(this.viewPermissions),
                    },
                  ],
                })
                .then(() => {});
            },
            err => {
              console.log(err);
            }
          );

          const requestedUrl = sessionStorage.getItem('requested-url');
          if (requestedUrl === '/user/login' || !requestedUrl) {
            this.router.replace('/');
          } else {
            this.router.replace(requestedUrl);
          }
          sessionStorage.removeItem('requested-url');
        } else {
          this.store.commit('logout');
          this.router.push('/');
          sessionStorage.removeItem('requested-url');
        }
        this.translationService.refreshTranslation(this.store.getters.currentLanguage);
      },
      error => {
        this.store.commit('logout');
        this.router.push('/');
      }
    );
  }

  public hasAnyAuthority(authorities: any): boolean {
    if (typeof authorities === 'string') {
      authorities = [authorities];
    }
    if (!this.authenticated || !this.userAuthorities) {
      return false;
    }

    for (let i = 0; i < authorities.length; i++) {
      if (this.userAuthorities.find(authority => authority.code === authorities[i])) {
        return true;
      }
    }

    return false;
  }

  public get authenticated(): boolean {
    return this.store.getters.authenticated;
  }

  public get userAuthorities(): any {
    return this.store.getters.account.authorities;
  }

  transformViewPermissionsToMenus(viewPermissions: ViewPermission[]) {
    const result = [];
    viewPermissions.forEach(viewPermission => {
      const menu: any = {};
      menu.disabled = viewPermission.disabled;
      menu.externalLink = viewPermission.externalLink;
      menu.group = viewPermission.group;
      menu.hide = viewPermission.hide;
      menu.hideInBreadcrumb = viewPermission.hideInBreadcrumb;
      menu.i18n = viewPermission.i18n;
      if (menu.externalLink) {
        switch (viewPermission.target) {
          case TargetType.BLANK:
            menu['meta'] = { target: '_blank' };
            break;
          case TargetType.PARENT:
            menu['meta'] = { target: '_parent' };
            break;
          case TargetType.SELF:
            menu['meta'] = { target: '_self' };
            break;
          case TargetType.TOP:
            menu['meta'] = { target: '_top' };
            break;
          default:
            menu['meta'] = {};
        }
      } else {
        menu['meta'] = {};
      }
      menu.text = viewPermission.text;
      menu.shortcut = viewPermission.shortcut;
      menu.shortcutRoot = viewPermission.shortcutRoot;
      menu.reuse = viewPermission.reuse;
      menu.name = viewPermission.code;
      // menu.redirect = viewPermission.link; // 可能影响导航，导致整个页面刷新
      menu['meta'].title = viewPermission.text;
      // todo menu['meta'].authorities = viewPermission.authorities;
      const icon = viewPermission.icon ? viewPermission.icon.replace('anticon anticon-', '') : viewPermission.icon;
      if (icon) {
        menu['meta'].icon = icon;
      }
      menu.path = viewPermission.link;
      if (viewPermission.children) {
        const tempChildren = this.transformViewPermissionsToMenus(viewPermission.children);
        if (tempChildren && tempChildren.length > 0) {
          menu.children = tempChildren;
        }
      }
      result.push(menu);
    });
    return result;
  }
}
