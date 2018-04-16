import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MessageStatus } from './message-status.model';
import { MessageStatusService } from './message-status.service';

@Component({
    selector: 'jhi-message-status-detail',
    templateUrl: './message-status-detail.component.html'
})
export class MessageStatusDetailComponent implements OnInit, OnDestroy {

    messageStatus: MessageStatus;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private messageStatusService: MessageStatusService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMessageStatuses();
    }

    load(id) {
        this.messageStatusService.find(id)
            .subscribe((messageStatusResponse: HttpResponse<MessageStatus>) => {
                this.messageStatus = messageStatusResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMessageStatuses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'messageStatusListModification',
            (response) => this.load(this.messageStatus.id)
        );
    }
}
