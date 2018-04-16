import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { WordGroup } from './word-group.model';
import { WordGroupService } from './word-group.service';

@Component({
    selector: 'jhi-word-group-detail',
    templateUrl: './word-group-detail.component.html'
})
export class WordGroupDetailComponent implements OnInit, OnDestroy {

    wordGroup: WordGroup;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wordGroupService: WordGroupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWordGroups();
    }

    load(id) {
        this.wordGroupService.find(id)
            .subscribe((wordGroupResponse: HttpResponse<WordGroup>) => {
                this.wordGroup = wordGroupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWordGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wordGroupListModification',
            (response) => this.load(this.wordGroup.id)
        );
    }
}
