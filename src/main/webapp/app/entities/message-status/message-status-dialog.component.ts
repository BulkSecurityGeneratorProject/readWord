import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MessageStatus } from './message-status.model';
import { MessageStatusPopupService } from './message-status-popup.service';
import { MessageStatusService } from './message-status.service';
import { Message, MessageService } from '../message';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-message-status-dialog',
    templateUrl: './message-status-dialog.component.html'
})
export class MessageStatusDialogComponent implements OnInit {

    messageStatus: MessageStatus;
    isSaving: boolean;

    msgs: Message[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private messageStatusService: MessageStatusService,
        private messageService: MessageService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.messageService
            .query({filter: 'messagestatus-is-null'})
            .subscribe((res: HttpResponse<Message[]>) => {
                if (!this.messageStatus.msgId) {
                    this.msgs = res.body;
                } else {
                    this.messageService
                        .find(this.messageStatus.msgId)
                        .subscribe((subRes: HttpResponse<Message>) => {
                            this.msgs = [subRes.body].concat(res.body);
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
        if (this.messageStatus.id !== undefined) {
            this.subscribeToSaveResponse(
                this.messageStatusService.update(this.messageStatus));
        } else {
            this.subscribeToSaveResponse(
                this.messageStatusService.create(this.messageStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MessageStatus>>) {
        result.subscribe((res: HttpResponse<MessageStatus>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MessageStatus) {
        this.eventManager.broadcast({ name: 'messageStatusListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMessageById(index: number, item: Message) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-message-status-popup',
    template: ''
})
export class MessageStatusPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messageStatusPopupService: MessageStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.messageStatusPopupService
                    .open(MessageStatusDialogComponent as Component, params['id']);
            } else {
                this.messageStatusPopupService
                    .open(MessageStatusDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
