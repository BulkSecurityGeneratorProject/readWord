import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AudioComponent } from './audio.component';
import { AudioDetailComponent } from './audio-detail.component';
import { AudioPopupComponent } from './audio-dialog.component';
import { AudioDeletePopupComponent } from './audio-delete-dialog.component';

@Injectable()
export class AudioResolvePagingParams implements Resolve<any> {

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

export const audioRoute: Routes = [
    {
        path: 'audio',
        component: AudioComponent,
        resolve: {
            'pagingParams': AudioResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.audio.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'audio/:id',
        component: AudioDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.audio.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const audioPopupRoute: Routes = [
    {
        path: 'audio-new',
        component: AudioPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.audio.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'audio/:id/edit',
        component: AudioPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.audio.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'audio/:id/delete',
        component: AudioDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'readWordApp.audio.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
