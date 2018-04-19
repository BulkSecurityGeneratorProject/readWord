import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService, JhiDataUtils} from 'ng-jhipster';

import {Word} from './word.model';
import {WordPopupService} from './word-popup.service';
import {WordService} from './word.service';
import {Image, ImageService} from '../image';
import {Audio, AudioService} from '../audio';
import {User, UserService} from '../../shared';
import {WordGroup, WordGroupService} from '../word-group';
import {Favorite, FavoriteService} from '../favorite';

@Component({
    selector: 'jhi-word-dialog',
    templateUrl: './word-dialog.component.html'
})
export class WordDialogComponent implements OnInit {

    word: Word;
    isSaving: boolean;

    imgs: Image[];

    audio: Audio[];

    users: User[];

    wordgroups: WordGroup[];

    favorites: Favorite[];

    constructor(public activeModal: NgbActiveModal,
                private dataUtils: JhiDataUtils,
                private jhiAlertService: JhiAlertService,
                private wordService: WordService,
                private imageService: ImageService,
                private audioService: AudioService,
                private userService: UserService,
                private wordGroupService: WordGroupService,
                private favoriteService: FavoriteService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.imageService
            .query({filter: 'word-is-null', sort: ['id,desc']})
            .subscribe((res: HttpResponse<Image[]>) => {
                if (!this.word.imgId) {
                    this.imgs = res.body;
                } else {
                    this.imageService
                        .find(this.word.imgId)
                        .subscribe((subRes: HttpResponse<Image>) => {
                            this.imgs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.audioService
            .query({filter: 'word-is-null'})
            .subscribe((res: HttpResponse<Audio[]>) => {
                if (!this.word.audioId) {
                    this.audio = res.body;
                } else {
                    this.audioService
                        .find(this.word.audioId)
                        .subscribe((subRes: HttpResponse<Audio>) => {
                            this.audio = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => {
                this.users = res.body;
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.wordGroupService.query()
            .subscribe((res: HttpResponse<WordGroup[]>) => {
                this.wordgroups = res.body;
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.favoriteService.query()
            .subscribe((res: HttpResponse<Favorite[]>) => {
                this.favorites = res.body;
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.word.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wordService.update(this.word));
        } else {
            this.subscribeToSaveResponse(
                this.wordService.create(this.word));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Word>>) {
        result.subscribe((res: HttpResponse<Word>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Word) {
        this.eventManager.broadcast({name: 'wordListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackImageById(index: number, item: Image) {
        return item.id;
    }

    trackAudioById(index: number, item: Audio) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackWordGroupById(index: number, item: WordGroup) {
        return item.id;
    }

    trackFavoriteById(index: number, item: Favorite) {
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
    selector: 'jhi-word-popup',
    template: ''
})
export class WordPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private wordPopupService: WordPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.wordPopupService
                    .open(WordDialogComponent as Component, params['id']);
            } else {
                this.wordPopupService
                    .open(WordDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
