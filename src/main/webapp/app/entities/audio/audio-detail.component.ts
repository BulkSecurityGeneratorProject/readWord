import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Audio } from './audio.model';
import { AudioService } from './audio.service';

@Component({
    selector: 'jhi-audio-detail',
    templateUrl: './audio-detail.component.html'
})
export class AudioDetailComponent implements OnInit, OnDestroy {

    audio: Audio;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private audioService: AudioService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAudio();
    }

    load(id) {
        this.audioService.find(id)
            .subscribe((audioResponse: HttpResponse<Audio>) => {
                this.audio = audioResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAudio() {
        this.eventSubscriber = this.eventManager.subscribe(
            'audioListModification',
            (response) => this.load(this.audio.id)
        );
    }
}
