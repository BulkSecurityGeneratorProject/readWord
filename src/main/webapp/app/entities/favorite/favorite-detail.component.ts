import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Favorite } from './favorite.model';
import { FavoriteService } from './favorite.service';

@Component({
    selector: 'jhi-favorite-detail',
    templateUrl: './favorite-detail.component.html'
})
export class FavoriteDetailComponent implements OnInit, OnDestroy {

    favorite: Favorite;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private favoriteService: FavoriteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFavorites();
    }

    load(id) {
        this.favoriteService.find(id)
            .subscribe((favoriteResponse: HttpResponse<Favorite>) => {
                this.favorite = favoriteResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFavorites() {
        this.eventSubscriber = this.eventManager.subscribe(
            'favoriteListModification',
            (response) => this.load(this.favorite.id)
        );
    }
}
