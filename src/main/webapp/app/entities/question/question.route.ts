import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { QuestionComponent } from './question.component';
import { QuestionDetailComponent } from './question-detail.component';
import { QuestionPopupComponent } from './question-dialog.component';
import { QuestionDeletePopupComponent } from './question-delete-dialog.component';

@Injectable()
export class QuestionResolvePagingParams implements Resolve<any> {

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

export const questionRoute: Routes = [
    {
        path: 'question',
        component: QuestionComponent,
        resolve: {
            'pagingParams': QuestionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.question.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'question/:id',
        component: QuestionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.question.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionPopupRoute: Routes = [
    {
        path: 'question-new',
        component: QuestionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.question.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'question/:id/edit',
        component: QuestionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.question.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'question/:id/delete',
        component: QuestionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.question.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
