import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import {
    MessageContentService,
    MessageContentPopupService,
    MessageContentComponent,
    MessageContentDetailComponent,
    MessageContentDialogComponent,
    MessageContentPopupComponent,
    MessageContentDeletePopupComponent,
    MessageContentDeleteDialogComponent,
    messageContentRoute,
    messageContentPopupRoute,
    MessageContentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...messageContentRoute,
    ...messageContentPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MessageContentComponent,
        MessageContentDetailComponent,
        MessageContentDialogComponent,
        MessageContentDeleteDialogComponent,
        MessageContentPopupComponent,
        MessageContentDeletePopupComponent,
    ],
    entryComponents: [
        MessageContentComponent,
        MessageContentDialogComponent,
        MessageContentPopupComponent,
        MessageContentDeleteDialogComponent,
        MessageContentDeletePopupComponent,
    ],
    providers: [
        MessageContentService,
        MessageContentPopupService,
        MessageContentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordMessageContentModule {}
