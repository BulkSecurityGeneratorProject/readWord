import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MessageStatusComponent } from './message-status.component';
import { MessageStatusDetailComponent } from './message-status-detail.component';
import { MessageStatusPopupComponent } from './message-status-dialog.component';
import { MessageStatusDeletePopupComponent } from './message-status-delete-dialog.component';

@Injectable()
export class MessageStatusResolvePagingParams implements Resolve<any> {

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

export const messageStatusRoute: Routes = [
    {
        path: 'message-status',
        component: MessageStatusComponent,
        resolve: {
            'pagingParams': MessageStatusResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'message-status/:id',
        component: MessageStatusDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const messageStatusPopupRoute: Routes = [
    {
        path: 'message-status-new',
        component: MessageStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'message-status/:id/edit',
        component: MessageStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'message-status/:id/delete',
        component: MessageStatusDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.messageStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
