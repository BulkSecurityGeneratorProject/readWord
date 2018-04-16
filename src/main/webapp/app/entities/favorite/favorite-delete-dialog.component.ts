import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Favorite } from './favorite.model';
import { FavoritePopupService } from './favorite-popup.service';
import { FavoriteService } from './favorite.service';

@Component({
    selector: 'jhi-favorite-delete-dialog',
    templateUrl: './favorite-delete-dialog.component.html'
})
export class FavoriteDeleteDialogComponent {

    favorite: Favorite;

    constructor(
        private favoriteService: FavoriteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.favoriteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'favoriteListModification',
                content: 'Deleted an favorite'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-favorite-delete-popup',
    template: ''
})
export class FavoriteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private favoritePopupService: FavoritePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.favoritePopupService
                .open(FavoriteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
