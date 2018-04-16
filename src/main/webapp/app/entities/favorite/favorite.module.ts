import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    FavoriteService,
    FavoritePopupService,
    FavoriteComponent,
    FavoriteDetailComponent,
    FavoriteDialogComponent,
    FavoritePopupComponent,
    FavoriteDeletePopupComponent,
    FavoriteDeleteDialogComponent,
    favoriteRoute,
    favoritePopupRoute,
    FavoriteResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...favoriteRoute,
    ...favoritePopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FavoriteComponent,
        FavoriteDetailComponent,
        FavoriteDialogComponent,
        FavoriteDeleteDialogComponent,
        FavoritePopupComponent,
        FavoriteDeletePopupComponent,
    ],
    entryComponents: [
        FavoriteComponent,
        FavoriteDialogComponent,
        FavoritePopupComponent,
        FavoriteDeleteDialogComponent,
        FavoriteDeletePopupComponent,
    ],
    providers: [
        FavoriteService,
        FavoritePopupService,
        FavoriteResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordFavoriteModule {}
