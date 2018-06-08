import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { VipOrder } from './vip-order.model';
import { VipOrderService } from './vip-order.service';

@Component({
    selector: 'jhi-vip-order-detail',
    templateUrl: './vip-order-detail.component.html'
})
export class VipOrderDetailComponent implements OnInit, OnDestroy {

    vipOrder: VipOrder;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private vipOrderService: VipOrderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVipOrders();
    }

    load(id) {
        this.vipOrderService.find(id)
            .subscribe((vipOrderResponse: HttpResponse<VipOrder>) => {
                this.vipOrder = vipOrderResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVipOrders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vipOrderListModification',
            (response) => this.load(this.vipOrder.id)
        );
    }
}
