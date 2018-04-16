import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    MessageStatusService,
    MessageStatusPopupService,
    MessageStatusComponent,
    MessageStatusDetailComponent,
    MessageStatusDialogComponent,
    MessageStatusPopupComponent,
    MessageStatusDeletePopupComponent,
    MessageStatusDeleteDialogComponent,
    messageStatusRoute,
    messageStatusPopupRoute,
    MessageStatusResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...messageStatusRoute,
    ...messageStatusPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MessageStatusComponent,
        MessageStatusDetailComponent,
        MessageStatusDialogComponent,
        MessageStatusDeleteDialogComponent,
        MessageStatusPopupComponent,
        MessageStatusDeletePopupComponent,
    ],
    entryComponents: [
        MessageStatusComponent,
        MessageStatusDialogComponent,
        MessageStatusPopupComponent,
        MessageStatusDeleteDialogComponent,
        MessageStatusDeletePopupComponent,
    ],
    providers: [
        MessageStatusService,
        MessageStatusPopupService,
        MessageStatusResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordMessageStatusModule {}
