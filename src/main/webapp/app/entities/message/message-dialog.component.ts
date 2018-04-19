import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Message} from './message.model';
import {MessagePopupService} from './message-popup.service';
import {MessageService} from './message.service';
import {Image, ImageService} from '../image';
import {MessageContent, MessageContentService} from '../message-content';

@Component({
    selector: 'jhi-message-dialog',
    templateUrl: './message-dialog.component.html'
})
export class MessageDialogComponent implements OnInit {

    message: Message;
    isSaving: boolean;

    imgs: Image[];

    contents: MessageContent[];

    constructor(public activeModal: NgbActiveModal,
                private jhiAlertService: JhiAlertService,
                private messageService: MessageService,
                private imageService: ImageService,
                private messageContentService: MessageContentService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.imageService
            .query({filter: 'message-is-null', sort: ['id,desc']})
            .subscribe((res: HttpResponse<Image[]>) => {
                if (!this.message.imgId) {
                    this.imgs = res.body;
                } else {
                    this.imageService
                        .find(this.message.imgId)
                        .subscribe((subRes: HttpResponse<Image>) => {
                            this.imgs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.messageContentService
            .query({filter: 'message-is-null'})
            .subscribe((res: HttpResponse<MessageContent[]>) => {
                if (!this.message.contentId) {
                    this.contents = res.body;
                } else {
                    this.messageContentService
                        .find(this.message.contentId)
                        .subscribe((subRes: HttpResponse<MessageContent>) => {
                            this.contents = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.message.id !== undefined) {
            this.subscribeToSaveResponse(
                this.messageService.update(this.message));
        } else {
            this.subscribeToSaveResponse(
                this.messageService.create(this.message));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Message>>) {
        result.subscribe((res: HttpResponse<Message>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Message) {
        this.eventManager.broadcast({name: 'messageListModification', content: 'OK'});
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

    trackMessageContentById(index: number, item: MessageContent) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-message-popup',
    template: ''
})
export class MessagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private messagePopupService: MessagePopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.messagePopupService
                    .open(MessageDialogComponent as Component, params['id']);
            } else {
                this.messagePopupService
                    .open(MessageDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
