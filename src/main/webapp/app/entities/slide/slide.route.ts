import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SlideComponent } from './slide.component';
import { SlideDetailComponent } from './slide-detail.component';
import { SlidePopupComponent } from './slide-dialog.component';
import { SlideDeletePopupComponent } from './slide-delete-dialog.component';

@Injectable()
export class SlideResolvePagingParams implements Resolve<any> {

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

export const slideRoute: Routes = [
    {
        path: 'slide',
        component: SlideComponent,
        resolve: {
            'pagingParams': SlideResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'slide/:id',
        component: SlideDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const slidePopupRoute: Routes = [
    {
        path: 'slide-new',
        component: SlidePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'slide/:id/edit',
        component: SlidePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'slide/:id/delete',
        component: SlideDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
