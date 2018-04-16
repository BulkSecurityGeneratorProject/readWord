import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MessageContentComponent } from './message-content.component';
import { MessageContentDetailComponent } from './message-content-detail.component';
import { MessageContentPopupComponent } from './message-content-dialog.component';
import { MessageContentDeletePopupComponent } from './message-content-delete-dialog.component';

@Injectable()
export class MessageContentResolvePagingParams implements Resolve<any> {

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

export const messageContentRoute: Routes = [
    {
        path: 'message-content',
        component: MessageContentComponent,
        resolve: {
            'pagingParams': MessageContentResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageContent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'message-content/:id',
        component: MessageContentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageContent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const messageContentPopupRoute: Routes = [
    {
        path: 'message-content-new',
        component: MessageContentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageContent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'message-content/:id/edit',
        component: MessageContentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageContent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'message-content/:id/delete',
        component: MessageContentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageContent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
