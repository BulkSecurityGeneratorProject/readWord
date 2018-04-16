import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { WordComponent } from './word.component';
import { WordDetailComponent } from './word-detail.component';
import { WordPopupComponent } from './word-dialog.component';
import { WordDeletePopupComponent } from './word-delete-dialog.component';

@Injectable()
export class WordResolvePagingParams implements Resolve<any> {

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

export const wordRoute: Routes = [
    {
        path: 'word',
        component: WordComponent,
        resolve: {
            'pagingParams': WordResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.word.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'word/:id',
        component: WordDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.word.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wordPopupRoute: Routes = [
    {
        path: 'word-new',
        component: WordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.word.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'word/:id/edit',
        component: WordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.word.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'word/:id/delete',
        component: WordDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.word.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
