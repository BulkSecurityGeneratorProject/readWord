import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    QuestionService,
    QuestionPopupService,
    QuestionComponent,
    QuestionDetailComponent,
    QuestionDialogComponent,
    QuestionPopupComponent,
    QuestionDeletePopupComponent,
    QuestionDeleteDialogComponent,
    questionRoute,
    questionPopupRoute,
    QuestionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...questionRoute,
    ...questionPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        QuestionComponent,
        QuestionDetailComponent,
        QuestionDialogComponent,
        QuestionDeleteDialogComponent,
        QuestionPopupComponent,
        QuestionDeletePopupComponent,
    ],
    entryComponents: [
        QuestionComponent,
        QuestionDialogComponent,
        QuestionPopupComponent,
        QuestionDeleteDialogComponent,
        QuestionDeletePopupComponent,
    ],
    providers: [
        QuestionService,
        QuestionPopupService,
        QuestionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordQuestionModule {}
