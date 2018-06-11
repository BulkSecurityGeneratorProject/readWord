import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { VipOrder } from './vip-order.model';
import { VipOrderPopupService } from './vip-order-popup.service';
import { VipOrderService } from './vip-order.service';
import { Product, ProductService } from '../product';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-vip-order-dialog',
    templateUrl: './vip-order-dialog.component.html'
})
export class VipOrderDialogComponent implements OnInit {

    vipOrder: VipOrder;
    isSaving: boolean;

    products: Product[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private vipOrderService: VipOrderService,
        private productService: ProductService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productService
            .query({filter: 'viporder-is-null'})
            .subscribe((res: HttpResponse<Product[]>) => {
                if (!this.vipOrder.productId) {
                    this.products = res.body;
                } else {
                    this.productService
                        .find(this.vipOrder.productId)
                        .subscribe((subRes: HttpResponse<Product>) => {
                            this.products = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vipOrder.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vipOrderService.update(this.vipOrder));
        } else {
            this.subscribeToSaveResponse(
                this.vipOrderService.create(this.vipOrder));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<VipOrder>>) {
        result.subscribe((res: HttpResponse<VipOrder>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: VipOrder) {
        this.eventManager.broadcast({ name: 'vipOrderListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vip-order-popup',
    template: ''
})
export class VipOrderPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vipOrderPopupService: VipOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vipOrderPopupService
                    .open(VipOrderDialogComponent as Component, params['id']);
            } else {
                this.vipOrderPopupService
                    .open(VipOrderDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
