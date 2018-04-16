import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WordGroup } from './word-group.model';
import { WordGroupPopupService } from './word-group-popup.service';
import { WordGroupService } from './word-group.service';
import { Image, ImageService } from '../image';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-word-group-dialog',
    templateUrl: './word-group-dialog.component.html'
})
export class WordGroupDialogComponent implements OnInit {

    wordGroup: WordGroup;
    isSaving: boolean;

    imgs: Image[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wordGroupService: WordGroupService,
        private imageService: ImageService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.imageService
            .query({filter: 'wordgroup-is-null'})
            .subscribe((res: HttpResponse<Image[]>) => {
                if (!this.wordGroup.imgId) {
                    this.imgs = res.body;
                } else {
                    this.imageService
                        .find(this.wordGroup.imgId)
                        .subscribe((subRes: HttpResponse<Image>) => {
                            this.imgs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wordGroup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wordGroupService.update(this.wordGroup));
        } else {
            this.subscribeToSaveResponse(
                this.wordGroupService.create(this.wordGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<WordGroup>>) {
        result.subscribe((res: HttpResponse<WordGroup>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: WordGroup) {
        this.eventManager.broadcast({ name: 'wordGroupListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-word-group-popup',
    template: ''
})
export class WordGroupPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wordGroupPopupService: WordGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wordGroupPopupService
                    .open(WordGroupDialogComponent as Component, params['id']);
            } else {
                this.wordGroupPopupService
                    .open(WordGroupDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
