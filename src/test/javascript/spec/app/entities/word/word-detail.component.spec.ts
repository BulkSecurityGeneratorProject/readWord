/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { WordDetailComponent } from '../../../../../../main/webapp/app/entities/word/word-detail.component';
import { WordService } from '../../../../../../main/webapp/app/entities/word/word.service';
import { Word } from '../../../../../../main/webapp/app/entities/word/word.model';

describe('Component Tests', () => {

    describe('Word Management Detail Component', () => {
        let comp: WordDetailComponent;
        let fixture: ComponentFixture<WordDetailComponent>;
        let service: WordService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [WordDetailComponent],
                providers: [
                    WordService
                ]
            })
            .overrideTemplate(WordDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WordDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Word(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.word).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
