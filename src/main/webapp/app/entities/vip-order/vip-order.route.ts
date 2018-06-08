import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { VipOrderComponent } from './vip-order.component';
import { VipOrderDetailComponent } from './vip-order-detail.component';
import { VipOrderPopupComponent } from './vip-order-dialog.component';
import { VipOrderDeletePopupComponent } from './vip-order-delete-dialog.component';

@Injectable()
export class VipOrderResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const vipOrderRoute: Routes = [
    {
        path: 'vip-order',
        component: VipOrderComponent,
        resolve: {
            'pagingParams': VipOrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.vipOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vip-order/:id',
        component: VipOrderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.vipOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vipOrderPopupRoute: Routes = [
    {
        path: 'vip-order-new',
        component: VipOrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.vipOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vip-order/:id/edit',
        component: VipOrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.vipOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vip-order/:id/delete',
        component: VipOrderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.vipOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
