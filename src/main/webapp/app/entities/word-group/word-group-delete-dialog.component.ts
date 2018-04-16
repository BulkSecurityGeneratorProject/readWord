import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WordGroup } from './word-group.model';
import { WordGroupPopupService } from './word-group-popup.service';
import { WordGroupService } from './word-group.service';

@Component({
    selector: 'jhi-word-group-delete-dialog',
    templateUrl: './word-group-delete-dialog.component.html'
})
export class WordGroupDeleteDialogComponent {

    wordGroup: WordGroup;

    constructor(
        private wordGroupService: WordGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wordGroupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wordGroupListModification',
                content: 'Deleted an wordGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-word-group-delete-popup',
    template: ''
})
export class WordGroupDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wordGroupPopupService: WordGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wordGroupPopupService
                .open(WordGroupDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
