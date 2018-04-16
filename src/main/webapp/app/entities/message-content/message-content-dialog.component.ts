import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { MessageContent } from './message-content.model';
import { MessageContentPopupService } from './message-content-popup.service';
import { MessageContentService } from './message-content.service';

@Component({
    selector: 'jhi-message-content-dialog',
    templateUrl: './message-content-dialog.component.html'
})
export class MessageContentDialogComponent implements OnInit {

    messageContent: MessageContent;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private messageContentService: MessageContentService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
        if (this.messageContent.id !== undefined) {
            this.subscribeToSaveResponse(
                this.messageContentService.update(this.messageContent));
        } else {
            this.subscribeToSaveResponse(
                this.messageContentService.create(this.messageContent));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MessageContent>>) {
        result.subscribe((res: HttpResponse<MessageContent>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MessageContent) {
        this.eventManager.broadcast({ name: 'messageContentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-message-content-popup',
    template: ''
})
export class MessageContentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messageContentPopupService: MessageContentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.messageContentPopupService
                    .open(MessageContentDialogComponent as Component, params['id']);
            } else {
                this.messageContentPopupService
                    .open(MessageContentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
