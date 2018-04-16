import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { MessageContent } from './message-content.model';
import { MessageContentService } from './message-content.service';

@Component({
    selector: 'jhi-message-content-detail',
    templateUrl: './message-content-detail.component.html'
})
export class MessageContentDetailComponent implements OnInit, OnDestroy {

    messageContent: MessageContent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private messageContentService: MessageContentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMessageContents();
    }

    load(id) {
        this.messageContentService.find(id)
            .subscribe((messageContentResponse: HttpResponse<MessageContent>) => {
                this.messageContent = messageContentResponse.body;
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

    registerChangeInMessageContents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'messageContentListModification',
            (response) => this.load(this.messageContent.id)
        );
    }
}
