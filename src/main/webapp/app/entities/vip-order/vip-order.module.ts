import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReadWordSharedModule } from '../../shared';
import { ReadWordAdminModule } from '../../admin/admin.module';
import {
    VipOrderService,
    VipOrderPopupService,
    VipOrderComponent,
    VipOrderDetailComponent,
    VipOrderDialogComponent,
    VipOrderPopupComponent,
    VipOrderDeletePopupComponent,
    VipOrderDeleteDialogComponent,
    vipOrderRoute,
    vipOrderPopupRoute,
    VipOrderResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...vipOrderRoute,
    ...vipOrderPopupRoute,
];

@NgModule({
    imports: [
        ReadWordSharedModule,
        ReadWordAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VipOrderComponent,
        VipOrderDetailComponent,
        VipOrderDialogComponent,
        VipOrderDeleteDialogComponent,
        VipOrderPopupComponent,
        VipOrderDeletePopupComponent,
    ],
    entryComponents: [
        VipOrderComponent,
        VipOrderDialogComponent,
        VipOrderPopupComponent,
        VipOrderDeleteDialogComponent,
        VipOrderDeletePopupComponent,
    ],
    providers: [
        VipOrderService,
        VipOrderPopupService,
        VipOrderResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordVipOrderModule {}
