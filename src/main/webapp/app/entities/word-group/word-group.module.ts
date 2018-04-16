import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    WordGroupService,
    WordGroupPopupService,
    WordGroupComponent,
    WordGroupDetailComponent,
    WordGroupDialogComponent,
    WordGroupPopupComponent,
    WordGroupDeletePopupComponent,
    WordGroupDeleteDialogComponent,
    wordGroupRoute,
    wordGroupPopupRoute,
    WordGroupResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...wordGroupRoute,
    ...wordGroupPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WordGroupComponent,
        WordGroupDetailComponent,
        WordGroupDialogComponent,
        WordGroupDeleteDialogComponent,
        WordGroupPopupComponent,
        WordGroupDeletePopupComponent,
    ],
    entryComponents: [
        WordGroupComponent,
        WordGroupDialogComponent,
        WordGroupPopupComponent,
        WordGroupDeleteDialogComponent,
        WordGroupDeletePopupComponent,
    ],
    providers: [
        WordGroupService,
        WordGroupPopupService,
        WordGroupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordWordGroupModule {}
