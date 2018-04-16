import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Word } from './word.model';
import { WordService } from './word.service';

@Component({
    selector: 'jhi-word-detail',
    templateUrl: './word-detail.component.html'
})
export class WordDetailComponent implements OnInit, OnDestroy {

    word: Word;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private wordService: WordService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWords();
    }

    load(id) {
        this.wordService.find(id)
            .subscribe((wordResponse: HttpResponse<Word>) => {
                this.word = wordResponse.body;
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

    registerChangeInWords() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wordListModification',
            (response) => this.load(this.word.id)
        );
    }
}
