/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { WordComponent } from '../../../../../../main/webapp/app/entities/word/word.component';
import { WordService } from '../../../../../../main/webapp/app/entities/word/word.service';
import { Word } from '../../../../../../main/webapp/app/entities/word/word.model';

describe('Component Tests', () => {

    describe('Word Management Component', () => {
        let comp: WordComponent;
        let fixture: ComponentFixture<WordComponent>;
        let service: WordService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [WordComponent],
                providers: [
                    WordService
                ]
            })
            .overrideTemplate(WordComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WordComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Word(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.words[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
