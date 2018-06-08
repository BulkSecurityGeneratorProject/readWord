import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { VipOrder } from './vip-order.model';
import { VipOrderService } from './vip-order.service';

@Injectable()
export class VipOrderPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private vipOrderService: VipOrderService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.vipOrderService.find(id)
                    .subscribe((vipOrderResponse: HttpResponse<VipOrder>) => {
                        const vipOrder: VipOrder = vipOrderResponse.body;
                        vipOrder.createTime = this.datePipe
                            .transform(vipOrder.createTime, 'yyyy-MM-ddTHH:mm:ss');
                        vipOrder.paymentTime = this.datePipe
                            .transform(vipOrder.paymentTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.vipOrderModalRef(component, vipOrder);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.vipOrderModalRef(component, new VipOrder());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    vipOrderModalRef(component: Component, vipOrder: VipOrder): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.vipOrder = vipOrder;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
