import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Word } from './word.model';
import { WordPopupService } from './word-popup.service';
import { WordService } from './word.service';

@Component({
    selector: 'jhi-word-delete-dialog',
    templateUrl: './word-delete-dialog.component.html'
})
export class WordDeleteDialogComponent {

    word: Word;

    constructor(
        private wordService: WordService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wordService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wordListModification',
                content: 'Deleted an word'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-word-delete-popup',
    template: ''
})
export class WordDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wordPopupService: WordPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wordPopupService
                .open(WordDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
