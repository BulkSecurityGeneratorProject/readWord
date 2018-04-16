import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MessageContent } from './message-content.model';
import { MessageContentPopupService } from './message-content-popup.service';
import { MessageContentService } from './message-content.service';

@Component({
    selector: 'jhi-message-content-delete-dialog',
    templateUrl: './message-content-delete-dialog.component.html'
})
export class MessageContentDeleteDialogComponent {

    messageContent: MessageContent;

    constructor(
        private messageContentService: MessageContentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.messageContentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'messageContentListModification',
                content: 'Deleted an messageContent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-message-content-delete-popup',
    template: ''
})
export class MessageContentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messageContentPopupService: MessageContentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.messageContentPopupService
                .open(MessageContentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
