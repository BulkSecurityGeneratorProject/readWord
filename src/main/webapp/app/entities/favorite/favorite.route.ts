import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { FavoriteComponent } from './favorite.component';
import { FavoriteDetailComponent } from './favorite-detail.component';
import { FavoritePopupComponent } from './favorite-dialog.component';
import { FavoriteDeletePopupComponent } from './favorite-delete-dialog.component';

@Injectable()
export class FavoriteResolvePagingParams implements Resolve<any> {

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

export const favoriteRoute: Routes = [
    {
        path: 'favorite',
        component: FavoriteComponent,
        resolve: {
            'pagingParams': FavoriteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.favorite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'favorite/:id',
        component: FavoriteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.favorite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const favoritePopupRoute: Routes = [
    {
        path: 'favorite-new',
        component: FavoritePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.favorite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'favorite/:id/edit',
        component: FavoritePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.favorite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'favorite/:id/delete',
        component: FavoriteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.favorite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
