import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Slide } from './slide.model';
import { SlideService } from './slide.service';

@Component({
    selector: 'jhi-slide-detail',
    templateUrl: './slide-detail.component.html'
})
export class SlideDetailComponent implements OnInit, OnDestroy {

    slide: Slide;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private slideService: SlideService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSlides();
    }

    load(id) {
        this.slideService.find(id)
            .subscribe((slideResponse: HttpResponse<Slide>) => {
                this.slide = slideResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSlides() {
        this.eventSubscriber = this.eventManager.subscribe(
            'slideListModification',
            (response) => this.load(this.slide.id)
        );
    }
}
