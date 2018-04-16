import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { WordGroupComponent } from './word-group.component';
import { WordGroupDetailComponent } from './word-group-detail.component';
import { WordGroupPopupComponent } from './word-group-dialog.component';
import { WordGroupDeletePopupComponent } from './word-group-delete-dialog.component';

@Injectable()
export class WordGroupResolvePagingParams implements Resolve<any> {

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

export const wordGroupRoute: Routes = [
    {
        path: 'word-group',
        component: WordGroupComponent,
        resolve: {
            'pagingParams': WordGroupResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.wordGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'word-group/:id',
        component: WordGroupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.wordGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wordGroupPopupRoute: Routes = [
    {
        path: 'word-group-new',
        component: WordGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.wordGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'word-group/:id/edit',
        component: WordGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.wordGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'word-group/:id/delete',
        component: WordGroupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.wordGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
