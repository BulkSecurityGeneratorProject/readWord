import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import {
    SlideService,
    SlidePopupService,
    SlideComponent,
    SlideDetailComponent,
    SlideDialogComponent,
    SlidePopupComponent,
    SlideDeletePopupComponent,
    SlideDeleteDialogComponent,
    slideRoute,
    slidePopupRoute,
    SlideResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...slideRoute,
    ...slidePopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SlideComponent,
        SlideDetailComponent,
        SlideDialogComponent,
        SlideDeleteDialogComponent,
        SlidePopupComponent,
        SlideDeletePopupComponent,
    ],
    entryComponents: [
        SlideComponent,
        SlideDialogComponent,
        SlidePopupComponent,
        SlideDeleteDialogComponent,
        SlideDeletePopupComponent,
    ],
    providers: [
        SlideService,
        SlidePopupService,
        SlideResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordSlideModule {}
