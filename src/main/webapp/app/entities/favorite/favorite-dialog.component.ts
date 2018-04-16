import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Favorite } from './favorite.model';
import { FavoritePopupService } from './favorite-popup.service';
import { FavoriteService } from './favorite.service';
import { User, UserService } from '../../shared';
import { Word, WordService } from '../word';

@Component({
    selector: 'jhi-favorite-dialog',
    templateUrl: './favorite-dialog.component.html'
})
export class FavoriteDialogComponent implements OnInit {

    favorite: Favorite;
    isSaving: boolean;

    users: User[];

    words: Word[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private favoriteService: FavoriteService,
        private userService: UserService,
        private wordService: WordService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.wordService.query()
            .subscribe((res: HttpResponse<Word[]>) => { this.words = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.favorite.id !== undefined) {
            this.subscribeToSaveResponse(
                this.favoriteService.update(this.favorite));
        } else {
            this.subscribeToSaveResponse(
                this.favoriteService.create(this.favorite));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Favorite>>) {
        result.subscribe((res: HttpResponse<Favorite>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Favorite) {
        this.eventManager.broadcast({ name: 'favoriteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackWordById(index: number, item: Word) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-favorite-popup',
    template: ''
})
export class FavoritePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private favoritePopupService: FavoritePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.favoritePopupService
                    .open(FavoriteDialogComponent as Component, params['id']);
            } else {
                this.favoritePopupService
                    .open(FavoriteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
