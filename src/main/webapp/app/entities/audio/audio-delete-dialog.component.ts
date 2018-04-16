import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Audio } from './audio.model';
import { AudioPopupService } from './audio-popup.service';
import { AudioService } from './audio.service';

@Component({
    selector: 'jhi-audio-delete-dialog',
    templateUrl: './audio-delete-dialog.component.html'
})
export class AudioDeleteDialogComponent {

    audio: Audio;

    constructor(
        private audioService: AudioService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.audioService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'audioListModification',
                content: 'Deleted an audio'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-audio-delete-popup',
    template: ''
})
export class AudioDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private audioPopupService: AudioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.audioPopupService
                .open(AudioDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
