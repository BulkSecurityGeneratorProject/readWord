import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MessageStatus } from './message-status.model';
import { MessageStatusPopupService } from './message-status-popup.service';
import { MessageStatusService } from './message-status.service';

@Component({
    selector: 'jhi-message-status-delete-dialog',
    templateUrl: './message-status-delete-dialog.component.html'
})
export class MessageStatusDeleteDialogComponent {

    messageStatus: MessageStatus;

    constructor(
        private messageStatusService: MessageStatusService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.messageStatusService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'messageStatusListModification',
                content: 'Deleted an messageStatus'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-message-status-delete-popup',
    template: ''
})
export class MessageStatusDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messageStatusPopupService: MessageStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.messageStatusPopupService
                .open(MessageStatusDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
