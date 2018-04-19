import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Slide} from './slide.model';
import {SlidePopupService} from './slide-popup.service';
import {SlideService} from './slide.service';
import {Image, ImageService} from '../image';

@Component({
    selector: 'jhi-slide-dialog',
    templateUrl: './slide-dialog.component.html'
})
export class SlideDialogComponent implements OnInit {

    slide: Slide;
    isSaving: boolean;

    imgs: Image[];

    constructor(public activeModal: NgbActiveModal,
                private jhiAlertService: JhiAlertService,
                private slideService: SlideService,
                private imageService: ImageService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.imageService
            .query({filter: 'slide-is-null', sort: ['id,desc']})
            .subscribe((res: HttpResponse<Image[]>) => {
                if (!this.slide.imgId) {
                    this.imgs = res.body;
                } else {
                    this.imageService
                        .find(this.slide.imgId)
                        .subscribe((subRes: HttpResponse<Image>) => {
                            this.imgs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.slide.id !== undefined) {
            this.subscribeToSaveResponse(
                this.slideService.update(this.slide));
        } else {
            this.subscribeToSaveResponse(
                this.slideService.create(this.slide));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Slide>>) {
        result.subscribe((res: HttpResponse<Slide>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Slide) {
        this.eventManager.broadcast({name: 'slideListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackImageById(index: number, item: Image) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-slide-popup',
    template: ''
})
export class SlidePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private slidePopupService: SlidePopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.slidePopupService
                    .open(SlideDialogComponent as Component, params['id']);
            } else {
                this.slidePopupService
                    .open(SlideDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
