import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VipOrder } from './vip-order.model';
import { VipOrderPopupService } from './vip-order-popup.service';
import { VipOrderService } from './vip-order.service';

@Component({
    selector: 'jhi-vip-order-delete-dialog',
    templateUrl: './vip-order-delete-dialog.component.html'
})
export class VipOrderDeleteDialogComponent {

    vipOrder: VipOrder;

    constructor(
        private vipOrderService: VipOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vipOrderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vipOrderListModification',
                content: 'Deleted an vipOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vip-order-delete-popup',
    template: ''
})
export class VipOrderDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vipOrderPopupService: VipOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vipOrderPopupService
                .open(VipOrderDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
