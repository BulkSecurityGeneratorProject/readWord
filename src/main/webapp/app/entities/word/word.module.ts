import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    WordService,
    WordPopupService,
    WordComponent,
    WordDetailComponent,
    WordDialogComponent,
    WordPopupComponent,
    WordDeletePopupComponent,
    WordDeleteDialogComponent,
    wordRoute,
    wordPopupRoute,
    WordResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...wordRoute,
    ...wordPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WordComponent,
        WordDetailComponent,
        WordDialogComponent,
        WordDeleteDialogComponent,
        WordPopupComponent,
        WordDeletePopupComponent,
    ],
    entryComponents: [
        WordComponent,
        WordDialogComponent,
        WordPopupComponent,
        WordDeleteDialogComponent,
        WordDeletePopupComponent,
    ],
    providers: [
        WordService,
        WordPopupService,
        WordResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordWordModule {}
