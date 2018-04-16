/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ReadWordTestModule } from '../../../test.module';
import { WordDialogComponent } from '../../../../../../main/webapp/app/entities/word/word-dialog.component';
import { WordService } from '../../../../../../main/webapp/app/entities/word/word.service';
import { Word } from '../../../../../../main/webapp/app/entities/word/word.model';
import { ImageService } from '../../../../../../main/webapp/app/entities/image';
import { AudioService } from '../../../../../../main/webapp/app/entities/audio';
import { UserService } from '../../../../../../main/webapp/app/shared';
import { WordGroupService } from '../../../../../../main/webapp/app/entities/word-group';
import { FavoriteService } from '../../../../../../main/webapp/app/entities/favorite';

describe('Component Tests', () => {

    describe('Word Management Dialog Component', () => {
        let comp: WordDialogComponent;
        let fixture: ComponentFixture<WordDialogComponent>;
        let service: WordService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [WordDialogComponent],
                providers: [
                    ImageService,
                    AudioService,
                    UserService,
                    WordGroupService,
                    FavoriteService,
                    WordService
                ]
            })
            .overrideTemplate(WordDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WordDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Word(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.word = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wordListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Word();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.word = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wordListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
