import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import {
    AudioService,
    AudioPopupService,
    AudioComponent,
    AudioDetailComponent,
    AudioDialogComponent,
    AudioPopupComponent,
    AudioDeletePopupComponent,
    AudioDeleteDialogComponent,
    audioRoute,
    audioPopupRoute,
    AudioResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...audioRoute,
    ...audioPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AudioComponent,
        AudioDetailComponent,
        AudioDialogComponent,
        AudioDeleteDialogComponent,
        AudioPopupComponent,
        AudioDeletePopupComponent,
    ],
    entryComponents: [
        AudioComponent,
        AudioDialogComponent,
        AudioPopupComponent,
        AudioDeleteDialogComponent,
        AudioDeletePopupComponent,
    ],
    providers: [
        AudioService,
        AudioPopupService,
        AudioResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordAudioModule {}
